package me.tatarka.bindingcollectionadapter2.recyclerview;

import android.support.test.espresso.core.deps.dagger.internal.Factory;
import android.support.test.espresso.core.deps.guava.base.Joiner;

import java.util.Iterator;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TestHelpers {

    public static class ViewModel {
        public final List<String> items;
        public final ItemBinding<String> itemBinding;
        public final BindingListViewAdapter.ItemIds<String> itemIds;
        public final MyBindingRecyclerViewAdapter<String> adapter = new MyBindingRecyclerViewAdapter<>();

        public ViewModel(List<String> items, ItemBinding<String> itemBinding) {
            this(items, itemBinding, null);
        }

        public ViewModel(List<String> items, ItemBinding<String> itemBinding, final List<Long> itemIds) {
            this.items = items;
            this.itemBinding = itemBinding;
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
        }
    }

    public static class MyBindingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
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
