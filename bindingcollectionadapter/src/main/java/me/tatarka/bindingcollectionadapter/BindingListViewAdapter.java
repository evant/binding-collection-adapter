package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
     */
    public static final String DROP_DOWN_LAYOUT = "drop_down_layout";

    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    // This is what the listview sees. It will only be modified on the main thread.
    private final List<T> boundItems = new ArrayList<>();
    private ObservableList<T> items;
    private int[] layouts;
    private int[] dropDownLayouts;
    private LayoutInflater inflater;
    private ItemIds<T> itemIds;
    private BindingCollectionListener<T> bindingCollectionListener;

    /**
     * Constructs a new instance with the given {@link ItemView}.
     */
    public BindingListViewAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    /**
     * Constructs a new instance with the given {@link ItemViewSelector}.
     */
    public BindingListViewAdapter(@NonNull ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }

    @Override
    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(callback);
            this.boundItems.clear();
            notifyDataSetChanged();
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            this.boundItems.addAll(items);
            notifyDataSetChanged();
            this.items.addOnListChangedCallback(callback);
        } else if (items != null) {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(callback);
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }
    
    @Override
    public void setBindingCollectionListener(@Nullable BindingCollectionListener<T> listener) {
        this.bindingCollectionListener = listener;
    }
    
    @Override
    public ObservableList<T> getItems() {
        return items;
    }

    /**
     * Set the item id's for the items.
     */
    public void setItemIds(@Nullable ItemIds<T> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public int getCount() {
        return boundItems.size();
    }

    @Override
    public T getItem(int position) {
        return boundItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemIds == null ? position : itemIds.getItemId(position, boundItems.get(position));
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);

        ViewDataBinding binding;
        if (convertView == null) {
            int layoutRes = layouts[viewType];
            binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            binding.getRoot().setTag(binding);
            if (bindingCollectionListener != null) {
                bindingCollectionListener.onBindingCreated(binding);
            }
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        if (itemView.getBindingVariable() != ItemView.BINDING_VARIABLE_NONE) {
            T item = boundItems.get(position);
            boolean result = binding.setVariable(itemView.getBindingVariable(), item);
            if (!result) {
                String layoutName = parent.getResources().getResourceName(itemView.getLayoutRes());
                throw new IllegalStateException("Could not bind variable on layout '" + layoutName + "'");
            }
            binding.executePendingBindings();
            if (bindingCollectionListener != null) {
                bindingCollectionListener.onBindingBound(binding, position, item);
            }
        }

        return binding.getRoot();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);
        int layoutRes = dropDownLayouts[viewType];
        if (layoutRes == 0) {
            return super.getDropDownView(position, convertView, parent);
        } else {
            ViewDataBinding binding;
            if (convertView == null) {
                binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
                binding.getRoot().setTag(binding);
            } else {
                binding = (ViewDataBinding) convertView.getTag();
            }

            if (itemView.getBindingVariable() != ItemView.BINDING_VARIABLE_NONE) {
                T item = boundItems.get(position);
                boolean result = binding.setVariable(itemView.getBindingVariable(), item);
                if (!result) {
                    String layoutName = parent.getResources().getResourceName(layoutRes);
                    throw new IllegalStateException("Could not bind variable on layout '" + layoutName + "'");
                }
                binding.executePendingBindings();
            }

            return binding.getRoot();
        }
    }

    @Override
    public int getItemViewType(int position) {
        selector.select(itemView, position, boundItems.get(position));
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
        dropDownLayouts[firstEmpty] = itemView.getLayoutRes(DROP_DOWN_LAYOUT);
        return firstEmpty;
    }

    @Override
    public boolean hasStableIds() {
        return itemIds != null;
    }

    @Override
    public int getViewTypeCount() {
        int count = selector.viewTypeCount();
        layouts = new int[count];
        dropDownLayouts = new int[count];
        return count;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends BaseOnListChangedCallback<T> {
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
            final List<T> changedItems = new ArrayList<>(adapter.items);
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingListViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.boundItems.clear();
                        adapter.boundItems.addAll(changedItems);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
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
