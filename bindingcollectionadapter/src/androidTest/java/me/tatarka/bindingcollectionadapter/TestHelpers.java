package me.tatarka.bindingcollectionadapter;

import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.dagger.internal.Factory;
import android.support.test.espresso.core.deps.guava.base.Joiner;

import java.util.Iterator;
import java.util.List;

public class TestHelpers {

    public static class ViewModel {
        public final List<String> items;
        public final ItemView itemView;
        public final ItemViewSelector<String> itemViewSelector;
        public final BindingListViewAdapter.ItemIds<String> itemIds;

        public ViewModel(List<String> items, ItemView itemView) {
            this(items, itemView, null);
        }

        public ViewModel(List<String> items, ItemView itemView, final List<Long> itemIds) {
            this.items = items;
            this.itemView = itemView;
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
                    itemView.set(itemView.bindingVariable, itemView.layoutRes);
                }
            };
        }
    }

    public static class MyBindingListViewAdapter<T> extends BindingListViewAdapter<T> {
        public MyBindingListViewAdapter(@NonNull ItemView itemView) {
            super(itemView);
        }

        public MyBindingListViewAdapter(@NonNull ItemViewSelector<T> selector) {
            super(selector);
        }

        public MyBindingListViewAdapter(@NonNull ItemViewArg<T> arg) {
            super(arg);
        }
    }

    public static class MyBindingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
        public MyBindingRecyclerViewAdapter(@NonNull ItemView itemView) {
            super(itemView);
        }

        public MyBindingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
            super(selector);
        }

        public MyBindingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
            super(arg);
        }
    }
    
    public static class MyBindingViewPagerAdapter<T> extends BindingViewPagerAdapter<T> {
        public MyBindingViewPagerAdapter(@NonNull ItemView itemView) {
            super(itemView);
        }

        public MyBindingViewPagerAdapter(@NonNull ItemViewSelector<T> selector) {
            super(selector);
        }

        public MyBindingViewPagerAdapter(@NonNull ItemViewArg<T> arg) {
            super(arg);
        }
    }

    public static <T> Iterable<T> iterable(final BindingListViewAdapter<T> adapter) {
        if (adapter == null) return null;
        return new IndexIterable<>(new Factory<IndexIterator<T>>() {
            @Override
            public IndexIterator<T> get() {
                return new IndexIterator<T>() {
                    @Override
                    int getCount() {
                        return adapter.getCount();
                    }

                    @Override
                    T get(int index) {
                        return adapter.getAdapterItem(index);
                    }
                };
            }
        });
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

    public static <T> Iterable<T> iterable(final BindingViewPagerAdapter<T> adapter) {
        if (adapter == null) return null;
        return new IndexIterable<>(new Factory<IndexIterator<T>>() {
            @Override
            public IndexIterator<T> get() {
                return new IndexIterator<T>() {
                    @Override
                    int getCount() {
                        return adapter.getCount();
                    }

                    @Override
                    T get(int index) {
                        return adapter.getAdapterItem(index);
                    }
                };
            }
        });
    }

    public static Iterable<Long> iterableIds(final BindingListViewAdapter<?> adapter) {
        if (adapter == null) return null;
        return new IndexIterable<>(new Factory<IndexIterator<Long>>() {
            @Override
            public IndexIterator<Long> get() {
                return new IndexIterator<Long>() {
                    @Override
                    int getCount() {
                        return adapter.getCount();
                    }

                    @Override
                    Long get(int index) {
                        return adapter.getItemId(index);
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
