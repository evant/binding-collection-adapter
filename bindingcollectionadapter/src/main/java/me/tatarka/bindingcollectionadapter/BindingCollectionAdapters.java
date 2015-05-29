package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.Collection;

/**
 * Created by evantatarka on 5/20/15.
 */
public class BindingCollectionAdapters {
    // Is this guaranteed to be unique? NOPE, but eh, view tags with keys are rarely used anyway.
    private static final int ITEMS_KEY = -2;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(ITEMS_KEY, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(RecyclerView recyclerView, ItemViewSelector<RecyclerItemView, T> itemViewSelector) {
        Collection<T> items = (Collection<T>) recyclerView.getTag(ITEMS_KEY);
        recyclerView.setAdapter(new BindingRecyclerViewAdapter<>(itemViewSelector, items));
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
            listView.setTag(ITEMS_KEY, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ListView listView, ItemViewSelector<ListItemView, T> itemViewSelector) {
        Collection<T> items = (Collection<T>) listView.getTag(ITEMS_KEY);
        listView.setAdapter(new BindingListViewAdapter<>(itemViewSelector, items));
    }
}
