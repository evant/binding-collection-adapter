package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.paging.AsyncPagingDataDiffer;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadStateAdapter;
import androidx.paging.PagedList;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffPagedObservableList;
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffPagedObservableListV3;
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
    @BindingAdapter(value = {"itemBinding", "items", "headerLoadStateAdapter", "footerLoadStateAdapter", "adapter", "itemIds", "viewHolder", "diffConfig"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      ItemBinding<? super T> itemBinding,
                                      PagingData<T> items,
                                      final LoadStateAdapter headerLoadStateAdapter,
                                      final LoadStateAdapter footerLoadStateAdapter,
                                      BindingRecyclerViewAdapter<T> adapter,
                                      BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,
                                      AsyncDifferConfig<T> diffConfig) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        RecyclerView.Adapter rootAdapter = recyclerView.getAdapter();

        BindingRecyclerViewAdapter oldAdapter = null;
        if (rootAdapter instanceof ConcatAdapter) {
            ConcatAdapter concatAdapter = (ConcatAdapter) rootAdapter;
            for (RecyclerView.Adapter childAdapter : concatAdapter.getAdapters()) {
                if (childAdapter instanceof BindingRecyclerViewAdapter) {
                    oldAdapter = (BindingRecyclerViewAdapter<T>) childAdapter;
                    break;
                }
            }
        } else if (rootAdapter instanceof BindingRecyclerViewAdapter) {
            oldAdapter = (BindingRecyclerViewAdapter<T>) rootAdapter;
        }

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
                if (diffConfig == null) {
                    diffConfig = new AsyncDifferConfig.Builder(new DiffUtil.ItemCallback() {
                        @Override
                        public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                            return false;
                        }

                        @Override
                        public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                            return false;
                        }
                    }).build();
                }
                list = new AsyncDiffPagedObservableListV3<>(diffConfig);
                if (headerLoadStateAdapter != null || footerLoadStateAdapter != null) {
                    list.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
                        @Override
                        public Unit invoke(CombinedLoadStates combinedLoadStates) {
                            if (headerLoadStateAdapter != null) {
                                headerLoadStateAdapter.setLoadState(combinedLoadStates.getPrepend());
                            }
                            if (footerLoadStateAdapter != null) {
                                footerLoadStateAdapter.setLoadState(combinedLoadStates.getAppend());
                            }
                            return Unit.INSTANCE;
                        }
                    });
                }
                recyclerView.setTag(R.id.bindingcollectiondapter_list_id, list);
                adapter.setItems(list);
            }
            list.update(Utils.findLifecycleOwner(recyclerView).getLifecycle(), items);
        }

        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            if (headerLoadStateAdapter != null && footerLoadStateAdapter != null) {
                recyclerView.setAdapter(new ConcatAdapter(headerLoadStateAdapter, adapter, footerLoadStateAdapter));
            } else if (headerLoadStateAdapter != null) {
                recyclerView.setAdapter(new ConcatAdapter(headerLoadStateAdapter, adapter));
            } else if (footerLoadStateAdapter != null) {
                recyclerView.setAdapter(new ConcatAdapter(adapter, footerLoadStateAdapter));
            } else {
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
