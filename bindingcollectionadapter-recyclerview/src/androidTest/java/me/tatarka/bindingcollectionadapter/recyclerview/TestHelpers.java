package me.tatarka.bindingcollectionadapter.recyclerview;

import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.dagger.internal.Factory;
import android.support.test.espresso.core.deps.guava.base.Joiner;
import android.support.v7.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;

import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;

public class TestHelpers {

    public static class ViewModel {
        public final List<String> items;
        public final ItemViewArg<?> itemView;
        public final ItemViewSelector<String> itemViewSelector;
        public final BindingListViewAdapter.ItemIds<String> itemIds;

        public ViewModel(List<String> items, ItemView itemView) {
            this(items, ItemViewArg.of(itemView), null);
        }

        public ViewModel(List<String> items, ItemViewSelector<?> itemView) {
            this(items, ItemViewArg.of(itemView), null);
        }

        public ViewModel(List<String> items, ItemViewArg<?> itemViewArg, final List<Long> itemIds) {
            this.items = items;
            this.itemView = itemViewArg;
            if (itemIds != null) {
                this.itemIds = new BindingListViewAdapter.ItemIds<String>() {
                    @Override
                    public long getItemId(int position, String item) {
                        return itemIds.get(position);
                    }
                };
            } else {
                this.itemIds = null;
            }
            this.itemViewSelector = new BaseItemViewSelector<String>() {
                @Override
                public void select(ItemView itemView, int position, String item) {
                    itemView.set(itemView.bindingVariable(), itemView.layoutRes());
                }
            };
        }
    }

    public static final BindingRecyclerViewAdapterFactory MY_RECYCLER_VIEW_ADAPTER_FACTORY = new BindingRecyclerViewAdapterFactory() {
        @Override
        public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
            return new MyBindingRecyclerViewAdapter<>(arg);
        }
    };

    public static class MyBindingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
        public MyBindingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
            super(arg);
        }
    }

    public static <T> Iterable<T> iterable(final BindingRecyclerViewAdapter<T> adapter) {
        if (adapter == null) return null;
        return new IndexIterable<>(new Factory<IndexIterator<T>>() {
            @Override
            public IndexIterator<T> get() {
                return new IndexIterator<T>() {
                    @Override
                    int getCount() {
                        return adapter.getItemCount();
                    }

                    @Override
                    T get(int index) {
                        return adapter.getAdapterItem(index);
                    }
                };
            }
        });
    }

    private static class IndexIterable<T> implements Iterable<T> {
        private Factory<IndexIterator<T>> iteratorFactory;

        private IndexIterable(Factory<IndexIterator<T>> iteratorFactory) {
            this.iteratorFactory = iteratorFactory;
        }

        @Override
        public Iterator<T> iterator() {
            return iteratorFactory.get();
        }

        @Override
        public String toString() {
            return iterator().toString();
        }
    }

    private static abstract class IndexIterator<T> implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < getCount();
        }

        @Override
        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "[" + Joiner.on(',').join(this) + "]";
        }

        abstract int getCount();

        abstract T get(int index);
    }
}
