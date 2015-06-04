package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewParent;
import android.widget.ListView;

import java.util.Collection;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(RecyclerView recyclerView, ItemView itemView) {
        Collection<T> items = (Collection<T>) recyclerView.getTag();
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemView);
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(RecyclerView recyclerView, ItemViewSelector<T> selector) {
        Collection<T> items = (Collection<T>) recyclerView.getTag();
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(selector);
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ListView listView, Collection<T> items) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            ListViewState<T> state = (ListViewState<T>) listView.getTag();
            if (state == null) state = new ListViewState<>();
            state.items = items;
            listView.setTag(state);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemIds")
    public static <T> void setItemIds(ListView listView, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter != null) {
            adapter.setItemIds(itemIds);
        } else {
            ListViewState<T> state = (ListViewState<T>) listView.getTag();
            if (state == null) state = new ListViewState<>();
            state.itemIds = itemIds;
            listView.setTag(state);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ListView listView, ItemView itemView) {
        ListViewState<T> state = (ListViewState<T>) listView.getTag();
        Collection<T> items = state == null ? null : state.items;
        BindingListViewAdapter.ItemIds<T> itemIds = state == null ? null : state.itemIds;
        BindingListViewAdapter<T> adapter = new BindingListViewAdapter<>(itemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        listView.setAdapter(adapter);
    }
    
    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ListView listView, ItemViewSelector<T> selector) {
        Collection<T> items = (Collection<T>) listView.getTag();
        BindingListViewAdapter<T> adapter = new BindingListViewAdapter<>(selector);
        adapter.setItems(items);
        listView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ViewPager viewPager, Collection<T> items) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            ViewPagerState<T> state = (ViewPagerState<T>) viewPager.getTag();
            if (state == null) state = new ViewPagerState<>();
            state.items = items;
            viewPager.setTag(state);
        }
    }
    
    @SuppressWarnings("unchecked")
    @BindingAdapter("pageTitles")
    public static <T> void setPageTitles(ViewPager viewPager, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter != null) {
            adapter.setPageTitles(pageTitles);
        } else {
            ViewPagerState<T> state = (ViewPagerState<T>) viewPager.getTag();
            if (state == null) state = new ViewPagerState<>();
            state.pageTitles = pageTitles;
            viewPager.setTag(state);
        }
    }
        
    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ViewPager viewPager, ItemView itemView) {
        ViewPagerState<T> state = (ViewPagerState<T>) viewPager.getTag();
        Collection<T> items = state == null ? null : state.items;
        BindingViewPagerAdapter.PageTitles<T> pageTitles = state == null ? null : state.pageTitles;
        BindingViewPagerAdapter<T> adapter = new BindingViewPagerAdapter<>(itemView);
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ViewPager viewPager, ItemViewSelector<T> selector) {
        Collection<T> items = (Collection<T>) viewPager.getTag();
        BindingViewPagerAdapter<T> adapter = new BindingViewPagerAdapter<>(selector);
        adapter.setItems(items);
        viewPager.setAdapter(adapter);
    }
    
    private static class ListViewState<T> {
        Collection<T> items;
        BindingListViewAdapter.ItemIds<T> itemIds;
    }

    private static class ViewPagerState<T> {
        Collection<T> items;
        BindingViewPagerAdapter.PageTitles<T> pageTitles;
    }
}
