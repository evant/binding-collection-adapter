package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} that binds items to layouts using the given {@link ItemView} or
 * {@link ItemViewSelector}. If you give it an {@link ObservableList} it will also updated itself
 * based on changes to that list.
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder> implements BindingCollectionAdapter<T> {
    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    // This is what the recyclerview sees. It will only be modified on the main thread.
    private final List<T> boundItems = new ArrayList<>();
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private BindingCollectionListener<T> bindingCollectionListener;

    /**
     * Constructs a new instance with the given {@link ItemView}.
     */
    public BindingRecyclerViewAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    /**
     * Constructs a new instance with the given {@link ItemViewSelector}.
     */
    public BindingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
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
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            this.boundItems.addAll(items);
            notifyItemRangeInserted(0, this.items.size());
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
    public ObservableList<T> getItems() {
        return items;
    }
    
    @Override
    public void setBindingCollectionListener(@Nullable BindingCollectionListener<T> listener) {
        this.bindingCollectionListener = listener;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        callback.cancel();
        if (items != null) {
            items.removeOnListChangedCallback(callback);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
        if (bindingCollectionListener != null) {
            bindingCollectionListener.onBindingCreated(binding);
        }
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (itemView.getBindingVariable() != ItemView.BINDING_VARIABLE_NONE) {
            T item = boundItems.get(position);
            boolean result = viewHolder.binding.setVariable(itemView.getBindingVariable(), item);
            if (!result) {
                String layoutName = viewHolder.itemView.getResources().getResourceName(itemView.getLayoutRes());
                throw new IllegalStateException("Could not bind variable on layout '" + layoutName + "'");
            }
            viewHolder.binding.executePendingBindings();
            if (bindingCollectionListener != null) {
                bindingCollectionListener.onBindingBound(viewHolder.binding, position, item);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        selector.select(itemView, position, boundItems.get(position));
        return itemView.getLayoutRes();
    }

    @Override
    public int getItemCount() {
        return boundItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends BaseOnListChangedCallback<T> {
        final WeakReference<BindingRecyclerViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        public void cancel() {
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            final List<T> changedItems = new ArrayList<>(adapter.items);
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.boundItems.clear();
                        adapter.boundItems.addAll(changedItems);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            final List<T> changedItems = new ArrayList<>(itemCount);
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                changedItems.add(adapter.items.get(positionStart));
            }
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        for (int i = positionStart; i < positionStart + itemCount; i++) {
                            adapter.boundItems.set(i, changedItems.get(i - positionStart));
                        }
                        adapter.notifyItemRangeChanged(positionStart, itemCount);
                    }
                }
            });
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            final List<T> changedItems = new ArrayList<>(itemCount);
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                changedItems.add(adapter.items.get(i));
            }
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        for (int i = positionStart; i < positionStart + itemCount; i++) {
                            adapter.boundItems.add(i, changedItems.get(i - positionStart));
                        }
                        adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }
                }
            });
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            final List<T> changedItems = new ArrayList<>(itemCount);
            for (int i = fromPosition; i < fromPosition + itemCount; i++) {
                changedItems.add(adapter.items.get(i));
            }
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        for (int i = fromPosition + itemCount - 1; i >= fromPosition; i--) {
                            adapter.boundItems.remove(fromPosition);
                        }
                        adapter.boundItems.addAll(toPosition, changedItems);
                        for (int i = 0; i < itemCount; i++) {
                            adapter.notifyItemMoved(fromPosition + i, toPosition + i);
                        }
                    }
                }
            });
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        for (int i = positionStart + itemCount - 1; i >= positionStart; i--) {
                            adapter.boundItems.remove(i);
                        }
                        adapter.notifyItemRangeRemoved(positionStart, itemCount);
                    }
                }
            });
        }
    }
}
