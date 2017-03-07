package me.tatarka.bindingcollectionadapter2;

import android.support.test.espresso.core.deps.dagger.internal.Factory;
import android.support.test.espresso.core.deps.guava.base.Joiner;

import java.util.Iterator;
import java.util.List;

public class TestHelpers {

    public static class ViewModel {
        public final List<String> items;
        public final ItemBinding<String> itemBinding;
        public final BindingListViewAdapter.ItemIds<String> itemIds;
        public final BindingListViewAdapter.ItemIsEnabled<String> itemIsEnabled;

        public final BindingListViewAdapter<String> listAdapter = new MyBindingListViewAdapter<>(1);
        public final BindingViewPagerAdapter<String> pagerAdapter = new MyBindingViewPagerAdapter<>();

        private ViewModel(List<String> items, ItemBinding<String> itemBinding, final List<Long> itemIds, final List<Boolean> itemIsEnabled) {
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
            if (itemIsEnabled != null) {
                this.itemIsEnabled = new BindingListViewAdapter.ItemIsEnabled<String>() {
                    @Override
                    public boolean isEnabled(int position, String item) {
                        return itemIsEnabled.get(position);
                    }
                };
            } else {
                this.itemIsEnabled = null;
            }
        }

        public static class Builder {
            private List<String> items;
            private ItemBinding<String> itemBinding;
            private List<Long> itemIds;
            private List<Boolean> itemIsEnabled;

            public Builder(List<String> items, ItemBinding<String> itemBinding) {
                this.items = items;
                this.itemBinding = itemBinding;
            }

            public Builder itemIds(List<Long> itemIds) {
                this.itemIds = itemIds;
                return this;
            }

            public Builder itemIsEnabled(List<Boolean> itemIsEnabled) {
                this.itemIsEnabled = itemIsEnabled;
                return this;
            }

            public ViewModel build() {
                return new ViewModel(items, itemBinding, itemIds, itemIsEnabled);
            }
        }
    }

    public static class MyBindingListViewAdapter<T> extends BindingListViewAdapter<T> {
        public MyBindingListViewAdapter(int itemTypeCount) {
            super(itemTypeCount);
        }
    }

    public static class MyBindingViewPagerAdapter<T> extends BindingViewPagerAdapter<T> {
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

    public static Iterable<Boolean> iterableIsEnabled(final BindingListViewAdapter<?> adapter) {
        if (adapter == null) return null;
        return new IndexIterable<>(new Factory<IndexIterator<Boolean>>() {
            @Override
            public IndexIterator<Boolean> get() {
                return new IndexIterator<Boolean>() {
                    @Override
                    int getCount() {
                        return adapter.getCount();
                    }

                    @Override
                    Boolean get(int index) {
                        return adapter.isEnabled(index);
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
