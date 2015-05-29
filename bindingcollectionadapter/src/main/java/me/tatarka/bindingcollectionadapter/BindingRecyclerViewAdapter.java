package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.OnListChangedListener;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by evan on 5/16/15.
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder> {
    private final WeakReferenceOnListChangedListener listener = new WeakReferenceOnListChangedListener(this);
    private final ItemViewSelector<RecyclerItemView, T> selector;
    private final RecyclerItemView itemView = new RecyclerItemView();
    private ObservableList<T> items;
    private LayoutInflater inflater;

    public BindingRecyclerViewAdapter(ItemViewSelector<RecyclerItemView, T> selector, @Nullable Collection<T> items) {
        if (selector == null) {
            throw new IllegalArgumentException("ListItemViewSelector must not be null");
        }
        this.selector = selector;
        setItems(items);
    }

    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedListener(listener);
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            notifyItemRangeInserted(0, this.items.size());
            this.items.addOnListChangedListener(listener);
        } else if (items != null) {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedListener(listener);
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }

    public ObservableList<T> getItems() {
        return items;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        listener.cancel();
        if (items != null) {
            items.removeOnListChangedListener(listener);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        T item = items.get(position);
        viewHolder.binding.setVariable(itemView.getBindingVariable(), item);
    }

    @Override
    public int getItemViewType(int position) {
        selector.select(itemView, position, items.get(position));
        return itemView.getLayoutRes();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class WeakReferenceOnListChangedListener implements OnListChangedListener {
        final WeakReference<RecyclerView.Adapter> adapterRef;
        final Handler handler = new Handler(Looper.getMainLooper());

        WeakReferenceOnListChangedListener(RecyclerView.Adapter adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.Adapter adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.Adapter adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.notifyItemRangeChanged(positionStart, itemCount);
                    }
                }
            });
        }

        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.Adapter adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }
                }
            });
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition, final int itemCount) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.Adapter adapter = adapterRef.get();
                    if (adapter != null) {
                        for (int i = 0; i < itemCount; i++) {
                            adapter.notifyItemMoved(fromPosition + i, toPosition + i);
                        }
                    }
                }
            });
        }

        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.Adapter adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.notifyItemRangeRemoved(positionStart, itemCount);
                    }
                }
            });
        }

        public void cancel() {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
