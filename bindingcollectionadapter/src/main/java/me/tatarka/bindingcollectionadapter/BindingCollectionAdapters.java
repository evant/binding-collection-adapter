package me.tatarka.bindingcollectionadapter;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.Collection;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemView itemView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new BindingRecyclerViewAdapter<>(itemView);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewSelector<T> selector, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new BindingRecyclerViewAdapter<>(selector);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, String adapterClassName, ItemView itemView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, itemView);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, selector);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ListView listView, ItemView itemView, Collection<T> items) {
        setAdapter(listView, itemView, items, null);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ListView listView, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(listView, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter == null) {
            adapter = new BindingListViewAdapter<T>(itemView);
            listView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter == null) {
            adapter = new BindingListViewAdapter<T>(selector);
            listView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemView itemView, Collection<T> items) {
        setAdapter(listView, adapterClassName, itemView, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(listView, adapterClassName, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, itemView);
            listView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, selector);
            listView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemView itemView, Collection<T> items) {
        setAdapter(viewPager, itemView, items, null);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(viewPager, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemView itemView, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = new BindingViewPagerAdapter<T>(itemView);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> selector, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = new BindingViewPagerAdapter<T>(selector);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemView itemView, Collection<T> items) {
        setAdapter(viewPager, adapterClassName, itemView, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(viewPager, adapterClassName, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemView itemView, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, itemView);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, selector);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BindingCollectionAdapter> T createAdapter(String className, ItemView itemView) {
        try {
            return (T) Class.forName(className).getConstructor(ItemView.class).newInstance(itemView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends BindingCollectionAdapter> T createAdapter(String className, ItemViewSelector<?> selector) {
        try {
            return (T) Class.forName(className).getConstructor(ItemViewSelector.class).newInstance(selector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper to throw an exception when {@link android.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     */
    public static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Resources resources = binding.getRoot().getResources();
        String layoutName = resources.getResourceName(layoutRes);
        throw new IllegalStateException("Could not bind variable '" + bindingVariable + "' in layout '" + layoutName + "'");
    }
}
