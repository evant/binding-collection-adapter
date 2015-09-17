package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

/**
 * A {@link BaseAdapter} that binds items to layouts using the given {@link ItemView} or {@link
 * ItemViewSelector}. If you give it an {@link ObservableList} it will also updated itself based on
 * changes to that list.
 */
public class BindingListViewAdapter<T> extends BaseAdapter implements BindingCollectionAdapter<T> {
    /**
     * Pass this constant to {@link ItemView#setLayoutRes(String, int)} to set a drop down layout
     * res for the given item.
     *
     * @see #getDropDownView(int, View, ViewGroup)
     * @deprecated See {@link ItemView#setLayoutRes(String, int)}
     */
    @Deprecated
    public static final String DROP_DOWN_LAYOUT = "drop_down_layout";

    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    @Nullable
    private ItemView dropDownItemView;
    @NonNull
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private List<T> items;
    private int[] layouts;
    private LayoutInflater inflater;
    private ItemIds<T> itemIds;

    /**
     * Constructs a new instance with the given {@link ItemView}.
     *
     * @deprecated use {@link #BindingListViewAdapter(ItemViewArg)} instead.
     */
    @Deprecated
    public BindingListViewAdapter(@NonNull ItemView itemView) {
        this(ItemViewArg.<T>of(itemView));
    }

    /**
     * Constructs a new instance with the given {@link ItemViewSelector}.
     *
     * @deprecated use {@link #BindingListViewAdapter(ItemViewArg)}L instead.
     */
    @Deprecated
    public BindingListViewAdapter(@NonNull ItemViewSelector<T> selector) {
        this(ItemViewArg.of(selector));
    }

    /**
     * Constructs a new instance with the given {@link ItemViewArg}.
     */
    public BindingListViewAdapter(@NonNull ItemViewArg<T> arg) {
        this.itemView = arg.itemView;
        this.selector = arg.selector;
    }

    /**
     * Set a different {@link ItemView} to show for {@link #getDropDownView(int, View, ViewGroup)}.
     * If this is null, it will default to {@link #getView(int, View, ViewGroup)}.
     */
    public void setDropDownItemView(@Nullable ItemView itemView) {
        this.dropDownItemView = itemView;
    }

    @Override
    public void setItems(@Nullable Collection<T> items) {
        setItems(Utils.getListFromCollection(items));
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (this.items instanceof ObservableList) {
            ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        }
        if (items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {
        if (bindingVariable != ItemView.BINDING_VARIABLE_NONE) {
            boolean result = binding.setVariable(bindingVariable, item);
            if (!result) {
                Utils.throwMissingVariable(binding, bindingVariable, layoutRes);
            }
            binding.executePendingBindings();
        }
    }

    /**
     * Set the item id's for the items. If not null, this will make {@link #hasStableIds()} return
     * true.
     */
    public void setItemIds(@Nullable ItemIds<T> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemIds == null ? position : itemIds.getItemId(position, items.get(position));
    }

    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);
        int layoutRes = layouts[viewType];

        ViewDataBinding binding;
        if (convertView == null) {
            binding = onCreateBinding(inflater, layoutRes, parent);
            binding.getRoot().setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        T item = items.get(position);
        onBindBinding(binding, itemView.getBindingVariable(), layoutRes, position, item);

        return binding.getRoot();
    }

    @Override
    public final View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        if (dropDownItemView == null) {
            return super.getDropDownView(position, convertView, parent);
        } else {
            int layoutRes = dropDownItemView.layoutRes;
            ViewDataBinding binding;
            if (convertView == null) {
                binding = onCreateBinding(inflater, layoutRes, parent);
                binding.getRoot().setTag(binding);
            } else {
                binding = (ViewDataBinding) convertView.getTag();
            }

            T item = items.get(position);
            onBindBinding(binding, dropDownItemView.getBindingVariable(), layoutRes, position, item);

            return binding.getRoot();
        }
    }

    @Override
    public int getItemViewType(int position) {
        ensureLayoutsInit();
        T item = items.get(position);
        selector.select(itemView, position, item);

        int firstEmpty = 0;
        for (int i = 0; i < layouts.length; i++) {
            if (itemView.layoutRes == layouts[i]) {
                return i;
            }
            if (layouts[i] == 0) {
                firstEmpty = i;
            }
        }
        layouts[firstEmpty] = itemView.layoutRes;
        return firstEmpty;
    }

    @Override
    public boolean hasStableIds() {
        return itemIds != null;
    }

    @Override
    public int getViewTypeCount() {
        return ensureLayoutsInit();
    }

    private int ensureLayoutsInit() {
        int count = selector.viewTypeCount();
        if (layouts == null) {
            layouts = new int[count];
        }
        return count;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingListViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingListViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingListViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }
    }

    public interface ItemIds<T> {
        long getItemId(int position, T item);
    }
}
