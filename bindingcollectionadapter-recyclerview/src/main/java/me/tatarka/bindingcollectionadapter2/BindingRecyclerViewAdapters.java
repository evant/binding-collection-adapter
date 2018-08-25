package me.tatarka.bindingcollectionadapter2;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList;

/**
 * @see {@link BindingCollectionAdapters}
 */
public class BindingRecyclerViewAdapters {
    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      ItemBinding<T> itemBinding,
                                      List<T> items,
                                      BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      AsyncDifferConfig<T> diffConfig) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItemBinding(itemBinding);

        if (diffConfig != null && items != null) {
            AsyncDiffObservableList<T> list = (AsyncDiffObservableList<T>) adapter.items;
            if (list == null) {
                list = new AsyncDiffObservableList<>(diffConfig);
                adapter.setItems(list);
            }
            list.update(items);
        } else {
            adapter.setItems(items);
        }

        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingConversion
    public static <T> AsyncDifferConfig<T> toAsyncDifferConfig(DiffUtil.ItemCallback<T> callback) {
        return new AsyncDifferConfig.Builder<>(callback).build();
    }
}
