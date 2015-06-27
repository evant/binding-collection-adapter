package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import java.util.Collection;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingCollectionAdapter<T> adapter = (BindingCollectionAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            RecyclerViewState<T> viewState = (RecyclerViewState<T>) getViewState(recyclerView, recyclerViewStateFactory);
            viewState.items = items;
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(RecyclerView recyclerView, ItemView itemView) {
        RecyclerViewState<T> viewState = (RecyclerViewState<T>) getViewState(recyclerView, recyclerViewStateFactory);
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemView);
        adapter.setItems(viewState.items);
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(RecyclerView recyclerView, ItemViewSelector<T> selector) {
        RecyclerViewState<T> viewState = (RecyclerViewState<T>) getViewState(recyclerView, recyclerViewStateFactory);
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(selector);
        adapter.setItems(viewState.items);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ListView listView, Collection<T> items) {
        BindingCollectionAdapter<T> adapter = (BindingCollectionAdapter<T>) listView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            ListViewState<T> viewState = (ListViewState<T>) getViewState(listView, listViewStateFactory);
            viewState.items = items;
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemIds")
    public static <T> void setItemIds(ListView listView, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) listView.getAdapter();
        if (adapter != null) {
            adapter.setItemIds(itemIds);
            // We need to set the adapter again to force hasStableIds to be rechecked.
            listView.setAdapter(adapter);
        } else {
            ListViewState<T> viewState = (ListViewState<T>) getViewState(listView, listViewStateFactory);
            viewState.itemIds = itemIds;
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ListView listView, ItemView itemView) {
        ListViewState<T> viewState = (ListViewState<T>) getViewState(listView, listViewStateFactory);
        BindingListViewAdapter<T> adapter = new BindingListViewAdapter<>(itemView);
        adapter.setItems(viewState.items);
        adapter.setItemIds(viewState.itemIds);
        listView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ListView listView, ItemViewSelector<T> selector) {
        ListViewState<T> viewState = (ListViewState<T>) getViewState(listView, listViewStateFactory);
        BindingListViewAdapter<T> adapter = new BindingListViewAdapter<>(selector);
        adapter.setItems(viewState.items);
        listView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ViewPager viewPager, Collection<T> items) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            ViewPagerState<T> viewState = (ViewPagerState<T>) getViewState(viewPager, viewPagerStateFactory);
            viewState.items = items;
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("pageTitles")
    public static <T> void setPageTitles(ViewPager viewPager, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter != null) {
            adapter.setPageTitles(pageTitles);
        } else {
            ViewPagerState<T> viewState = (ViewPagerState<T>) getViewState(viewPager, viewPagerStateFactory);
            viewState.pageTitles = pageTitles;
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemView(ViewPager viewPager, ItemView itemView) {
        ViewPagerState<T> viewState = (ViewPagerState<T>) getViewState(viewPager, viewPagerStateFactory);
        BindingViewPagerAdapter<T> adapter = new BindingViewPagerAdapter<>(itemView);
        adapter.setItems(viewState.items);
        adapter.setPageTitles(viewState.pageTitles);
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemView")
    public static <T> void setItemViewSelector(ViewPager viewPager, ItemViewSelector<T> selector) {
        ViewPagerState<T> viewState = (ViewPagerState<T>) getViewState(viewPager, viewPagerStateFactory);
        BindingViewPagerAdapter<T> adapter = new BindingViewPagerAdapter<>(selector);
        adapter.setItems(viewState.items);
        adapter.setPageTitles(viewState.pageTitles);
        viewPager.setAdapter(adapter);
    }

    private static ViewStateFactory<RecyclerViewState> recyclerViewStateFactory = new ViewStateFactory<RecyclerViewState>() {
        @Override
        public RecyclerViewState newViewState() {
            return new RecyclerViewState<>();
        }
    };

    private static class RecyclerViewState<T> extends ViewState<T> {
    }

    private static ViewStateFactory<ListViewState> listViewStateFactory = new ViewStateFactory<ListViewState>() {
        @Override
        public ListViewState newViewState() {
            return new ListViewState<>();
        }
    };

    private static class ListViewState<T> extends ViewState<T> {
        BindingListViewAdapter.ItemIds<T> itemIds;
    }

    private static ViewStateFactory<ViewPagerState> viewPagerStateFactory = new ViewStateFactory<ViewPagerState>() {
        @Override
        public ViewPagerState newViewState() {
            return new ViewPagerState<>();
        }
    };

    private static class ViewPagerState<T> extends ViewState<T> {
        BindingViewPagerAdapter.PageTitles<T> pageTitles;
    }

    /**
     * Returns the view state for the given view, constructing it from the given factory if needed.
     */
    @SuppressWarnings("unchecked")
    public static <V extends ViewState> ViewState<?> getViewState(@NonNull View view, @NonNull ViewStateFactory<V> viewStateFactory) {
        ViewState<?> state = (ViewState<?>) view.getTag();
        if (state == null) {
            state = viewStateFactory.newViewState();
            view.setTag(state);
        }
        return state;
    }

    /**
     * Extra state for your adapter. This allows the state to be set before the adapter has been set
     * on the collection view.
     */
    public static abstract class ViewState<T> {
        public Collection<T> items;
    }

    /**
     * Factory for constructing view state when needed.
     */
    public interface ViewStateFactory<V extends ViewState> {
        V newViewState();
    }
}
