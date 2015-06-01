package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.Collection;

/**
 * Created by evantatarka on 5/20/15.
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
            listView.setTag(items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ListView listView, ItemView itemView) {
        Collection<T> items = (Collection<T>) listView.getTag();
        BindingListViewAdapter<T> adapter = new BindingListViewAdapter<>(itemView);
        adapter.setItems(items);
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
            viewPager.setTag(items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ViewPager viewPager, ItemView itemView) {
        Collection<T> items = (Collection<T>) viewPager.getTag();
        BindingViewPagerAdapter<T> adapter = new BindingViewPagerAdapter<>(itemView);
        adapter.setItems(items);
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
}
