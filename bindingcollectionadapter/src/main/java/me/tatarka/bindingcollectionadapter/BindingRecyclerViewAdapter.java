package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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
    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private ObservableList<T> items;
    private LayoutInflater inflater;

    public BindingRecyclerViewAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    public BindingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }

    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(callback);
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
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

    public ObservableList<T> getItems() {
        return items;
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

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<RecyclerView.Adapter> adapterRef;
        final Handler handler = new Handler(Looper.getMainLooper());

        WeakReferenceOnListChangedCallback(RecyclerView.Adapter adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        public void cancel() {
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public void onChanged(ObservableList sender) {
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
        public void onItemRangeChanged(ObservableList sender, final int positionStart, final int itemCount) {
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
        public void onItemRangeInserted(ObservableList sender, final int positionStart, final int itemCount) {
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
        public void onItemRangeMoved(ObservableList sender, final int fromPosition, final int toPosition, final int itemCount) {
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
        public void onItemRangeRemoved(ObservableList sender, final int positionStart, final int itemCount) {
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
    }
}
