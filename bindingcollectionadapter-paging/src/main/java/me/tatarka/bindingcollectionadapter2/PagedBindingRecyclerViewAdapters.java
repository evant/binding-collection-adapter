package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.paging.CombinedLoadStates;
import androidx.paging.PagedList;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.RecyclerView;

import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffPagedObservableList;
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffPagedObservableListV3;
import me.tatarka.bindingcollectionadapter2.collections.LoadStateListener;
import me.tatarka.bindingcollectionadapter2.collections.LoadStateObservableList;
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindLoadState;
import me.tatarka.bindingcollectionadapter2.paging.R;

/**
 * @see {@link BindingCollectionAdapters}
 */
public class PagedBindingRecyclerViewAdapters {
    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      ItemBinding<? super T> itemBinding,
                                      PagedList<T> items,
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
            AsyncDiffPagedObservableList<T> list = (AsyncDiffPagedObservableList<T>) recyclerView.getTag(R.id.bindingcollectiondapter_list_id);
            if (list == null) {
                list = new AsyncDiffPagedObservableList<>(diffConfig);
                recyclerView.setTag(R.id.bindingcollectiondapter_list_id, list);
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

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      OnItemBindLoadState<T> itemBinding,
                                      PagingData<T> items,
                                      BindingRecyclerViewAdapter<Object> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<Object> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      AsyncDifferConfig<T> diffConfig) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        if (diffConfig == null) {
            throw new IllegalArgumentException("diffConfig must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItemBinding(ItemBinding.of(itemBinding));

        if (items != null) {
            AsyncDiffPagedObservableListV3<T> list = (AsyncDiffPagedObservableListV3<T>) recyclerView.getTag(R.id.bindingcollectiondapter_list_id);
            if (list == null) {
                list = new AsyncDiffPagedObservableListV3<T>(diffConfig);
                final LoadStateObservableList<T> mergeList = new LoadStateObservableList<>(list);
                list.addLoadStateListener(new LoadStateListener() {
                    @Override
                    protected void onLoadStateChanged(@NonNull CombinedLoadStates loadStates) {
                        mergeList.setLoadStates(loadStates);
                    }
                });
                adapter.setItems(mergeList);
            }
            list.update(Utils.findLifecycleOwner(recyclerView).getLifecycle(), items);
        } else {
            adapter.setItems(null);
        }

        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      final ItemBinding<? super T> itemBinding,
                                      PagingData<T> items,
                                      BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      AsyncDifferConfig<T> diffConfig) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        if (diffConfig == null) {
            throw new IllegalArgumentException("diffConfig must not be null");
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

        if (items != null) {
            AsyncDiffPagedObservableListV3<T> list = (AsyncDiffPagedObservableListV3<T>) recyclerView.getTag(R.id.bindingcollectiondapter_list_id);
            if (list == null) {
                list = new AsyncDiffPagedObservableListV3<>(diffConfig);
                recyclerView.setTag(R.id.bindingcollectiondapter_list_id, list);
                adapter.setItems(list);
            }
            list.update(Utils.findLifecycleOwner(recyclerView).getLifecycle(), items);

        } else {
            adapter.setItems(null);
        }

        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }
}
