package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.Collection;

import me.tatarka.bindingcollectionadapter.BindingCollectionAdapters;
import me.tatarka.bindingcollectionadapter.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.LayoutManagers;

/**
 * BindingAdapters from BindingCollectionAdapter, used to work around android-apt issue.
 */
public class CopiedBindingCollectionAdapters {
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(recyclerView, itemView, items);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(recyclerView, selector, items);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, String adapterClassName, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(recyclerView, adapterClassName, itemView, items);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(recyclerView, adapterClassName, selector, items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        BindingCollectionAdapters.setLayoutManager(recyclerView, layoutManagerFactory);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ListView listView, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(listView, itemView, items);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ListView listView, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(listView, selector, items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingCollectionAdapters.setAdapter(listView, itemView, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingCollectionAdapters.setAdapter(listView, selector, items, itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(listView, adapterClassName, itemView, items);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(listView, adapterClassName, selector, items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingCollectionAdapters.setAdapter(listView, adapterClassName, itemView, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(ListView listView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingCollectionAdapters.setAdapter(listView, adapterClassName, selector, items, itemIds);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(viewPager, itemView, items);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(viewPager, selector, items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemView itemView, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingCollectionAdapters.setAdapter(viewPager, itemView, items, pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> selector, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingCollectionAdapters.setAdapter(viewPager, selector, items, pageTitles);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemView itemView, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(viewPager, adapterClassName, itemView, items);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        BindingCollectionAdapters.setAdapter(viewPager, adapterClassName, selector, items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemView itemView, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingCollectionAdapters.setAdapter(viewPager, adapterClassName, itemView, items, pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingCollectionAdapters.setAdapter(viewPager, adapterClassName, selector, items, pageTitles);
    }
}
