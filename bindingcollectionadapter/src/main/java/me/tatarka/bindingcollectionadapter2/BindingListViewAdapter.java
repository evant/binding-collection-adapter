package me.tatarka.bindingcollectionadapter2;

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
import java.util.List;

/**
 * A {@link BaseAdapter} that binds items to layouts using the given {@link ItemBinding} If you give
 * it an {@link ObservableList} it will also updated itself based on changes to that list.
 */
public class BindingListViewAdapter<T> extends BaseAdapter implements BindingCollectionAdapter<T> {
    private final int itemTypeCount;
    private ItemBinding<T> itemBinding;
    @LayoutRes
    private int dropDownItemLayout;
    @NonNull
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private List<T> items;
    private int[] layouts;
    private LayoutInflater inflater;
    private ItemIds<? super T> itemIds;
    private ItemIsEnabled<? super T> itemIsEnabled;

    /**
     * Constructs a new instance with the given item count.
     */
    public BindingListViewAdapter(int itemTypeCount) {
        this.itemTypeCount = itemTypeCount;
    }

    @Override
    public void setItemBinding(ItemBinding<T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    @Override
    public ItemBinding<T> getItemBinding() {
        return itemBinding;
    }

    /**
     * Set a different layout to show for {@link #getDropDownView(int, View, ViewGroup)}. If this is
     * null, it will default to {@link #getView(int, View, ViewGroup)}.
     */
    public void setDropDownItemLayout(@LayoutRes int layoutRes) {
        dropDownItemLayout = layoutRes;
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
    public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
        }
    }

    /**
     * Set the item id's for the items. If not null, this will make {@link #hasStableIds()} return
     * true.
     */
    public void setItemIds(@Nullable ItemIds<? super T> itemIds) {
        this.itemIds = itemIds;
    }

    /**
     * Sets {@link #isEnabled(int)} for the items.
     */
    public void setItemIsEnabled(@Nullable ItemIsEnabled<? super T> itemIsEnabled) {
        this.itemIsEnabled = itemIsEnabled;
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
    public boolean isEnabled(int position) {
        return itemIsEnabled == null || itemIsEnabled.isEnabled(position, items.get(position));
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
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        T item = items.get(position);
        onBindBinding(binding, itemBinding.variableId(), layoutRes, position, item);

        return binding.getRoot();
    }

    @Override
    public final View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        if (dropDownItemLayout == 0) {
            return super.getDropDownView(position, convertView, parent);
        } else {
            int layoutRes = dropDownItemLayout;
            ViewDataBinding binding;
            if (convertView == null) {
                binding = onCreateBinding(inflater, layoutRes, parent);
            } else {
                binding = DataBindingUtil.getBinding(convertView);
            }

            T item = items.get(position);
            onBindBinding(binding, itemBinding.variableId(), layoutRes, position, item);

            return binding.getRoot();
        }
    }

    @Override
    public int getItemViewType(int position) {
        ensureLayoutsInit();
        T item = items.get(position);
        itemBinding.onItemBind(position, item);

        int firstEmpty = 0;
        for (int i = 0; i < layouts.length; i++) {
            if (itemBinding.layoutRes() == layouts[i]) {
                return i;
            }
            if (layouts[i] == 0) {
                firstEmpty = i;
            }
        }
        layouts[firstEmpty] = itemBinding.layoutRes();
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
        int count = itemTypeCount;
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

    public interface ItemIsEnabled<T> {
        boolean isEnabled(int position, T item);
    }
}
