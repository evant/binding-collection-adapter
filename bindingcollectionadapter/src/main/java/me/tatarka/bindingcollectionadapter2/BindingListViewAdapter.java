package me.tatarka.bindingcollectionadapter2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * A {@link BaseAdapter} that binds items to layouts using the given {@link ItemBinding} If you give
 * it an {@link ObservableList} it will also updated itself based on changes to that list.
 */
public class BindingListViewAdapter<T> extends BaseAdapter implements BindingCollectionAdapter<T> {
    private final int itemTypeCount;
    private ItemBinding<? super T> itemBinding;
    @LayoutRes
    private int dropDownItemLayout;
    private WeakReferenceOnListChangedCallback<T> callback;
    private List<T> items;
    private int[] layouts;
    private LayoutInflater inflater;
    @Nullable
    private ItemIds<? super T> itemIds;
    @Nullable
    private ItemIsEnabled<? super T> itemIsEnabled;
    @Nullable
    private LifecycleOwner lifecycleOwner;

    /**
     * Constructs a new instance with the given item count.
     */
    public BindingListViewAdapter(int itemTypeCount) {
        this.itemTypeCount = itemTypeCount;
    }

    /**
     * Constructs a new instance with the given item count and item binding.
     */
    public BindingListViewAdapter(int itemTypeCount, @NonNull ItemBinding<? super T> itemBinding) {
        this.itemTypeCount = itemTypeCount;
        this.itemBinding = itemBinding;
    }

    @Override
    public void setItemBinding(@NonNull ItemBinding<? super T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    /**
     * Sets the lifecycle owner of this adapter to work with {@link androidx.lifecycle.LiveData}.
     * This is normally not necessary, but due to an androidx limitation, you need to set this if
     * the containing view is <em>not</em> using databinding.
     */
    public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemBinding<? super T> getItemBinding() {
        if (itemBinding == null) {
            throw new NullPointerException("itemBinding == null");
        }
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
            callback = null;
        }
        if (items instanceof ObservableList) {
            callback = new WeakReferenceOnListChangedCallback<>(this, (ObservableList<T>) items);
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public ViewDataBinding onCreateBinding(@NonNull LayoutInflater inflater, @LayoutRes int layoutRes, @NonNull ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(@NonNull ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
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
    public final View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        tryGetLifecycleOwner(parent);

        int viewType = getItemViewType(position);
        int layoutRes = layouts[viewType];

        ViewDataBinding binding;
        if (convertView == null) {
            binding = onCreateBinding(inflater, layoutRes, parent);
            binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        View view = binding.getRoot();

        T item = items.get(position);
        onBindBinding(binding, itemBinding.variableId(), layoutRes, position, item);

        return view;
    }

    @Override
    public final View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        tryGetLifecycleOwner(parent);

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

            View view = binding.getRoot();

            T item = items.get(position);
            onBindBinding(binding, itemBinding.variableId(), layoutRes, position, item);

            return view;
        }
    }

    private void tryGetLifecycleOwner(View view) {
        if (lifecycleOwner == null || lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            lifecycleOwner = Utils.findLifecycleOwner(view);
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

    static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingListViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingListViewAdapter<T> adapter, ObservableList<T> items) {
            this.adapterRef = AdapterReferenceCollector.createRef(adapter, items, this);
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
