package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} that binds items to layouts using the given {@link ItemView} or
 * {@link ItemViewSelector}. If you give it an {@link ObservableList} it will also updated itself
 * based on changes to that list.
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder> implements BindingCollectionAdapter<T> {
    @NonNull
    private final ItemViewArg<T> itemViewArg;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private List<T> items;
    private LayoutInflater inflater;
    private ItemIds<T> itemIds;
    // Number of attached recyclerviews, we don't have to listen to notifications if there are none.
    private int recyclerViewRefCount = 0;

    /**
     * Constructs a new instance with the given {@link ItemViewArg}.
     */
    public BindingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
        this.itemViewArg = arg;
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        // If a recyclerview is listening, set up listeners. Otherwise wait until one is attached.
        // No need to make a sound if nobody is listening right?
        if (recyclerViewRefCount > 0) {
            if (this.items instanceof ObservableList) {
                ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
            }
            if (items instanceof ObservableList) {
                ((ObservableList<T>) items).addOnListChangedCallback(callback);
            }
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (recyclerViewRefCount == 0 && items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        recyclerViewRefCount += 1;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerViewRefCount -= 1;
        if (recyclerViewRefCount == 0 && items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).removeOnListChangedCallback(callback);
        }
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        ViewDataBinding binding = onCreateBinding(inflater, layoutId, viewGroup);
        return new ViewHolder(binding);
    }

    @Override
    public final void onBindViewHolder(ViewHolder viewHolder, int position) {
        T item = items.get(position);
        onBindBinding(viewHolder.binding, itemViewArg.bindingVariable(), itemViewArg.layoutRes(), position, item);
    }

    @Override
    public int getItemViewType(int position) {
        itemViewArg.select(position, items.get(position));
        return itemViewArg.layoutRes();
    }

    /**
     * Set the item id's for the items. If not null, this will set {@link
     * android.support.v7.widget.RecyclerView.Adapter#setHasStableIds(boolean)} to true.
     */
    public void setItemIds(@Nullable ItemIds<T> itemIds) {
        this.itemIds = itemIds;
        setHasStableIds(itemIds != null);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        return itemIds == null ? position : itemIds.getItemId(position, items.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingRecyclerViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            for (int i = 0; i < itemCount; i++) {
                adapter.notifyItemMoved(fromPosition + i, toPosition + i);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public interface ItemIds<T> {
        long getItemId(int position, T item);
    }
}
