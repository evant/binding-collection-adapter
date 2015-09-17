package me.tatarka.bindingcollectionadapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.Collection;

import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingViewPagerAdapterFactory;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    // There are quite a few arguments here so we need some rules for consistency.
    // 1) All arguments must be in the order: view, itemViewArg, items, factory, additional args
    // 2) There must be one method that takes all possible args. All other methods must call that 
    // one with no intermediate steps.
    // 3) Methods for a given view are order from least to greatest number of arguments.
    // 4) Methods with factory must come before items without (when they are the same length).
    // 5) Methods with an earlier argument in the master method must come before ones later 
    // (when they are the same length).

    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(recyclerView, arg, items, BindingRecyclerViewAdapterFactory.DEFAULT, null);
    }

    @BindingAdapter({"itemView", "items", "adapter"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, Collection<T> items, BindingRecyclerViewAdapterFactory factory) {
        setAdapter(recyclerView, arg, items, factory, null);
    }

    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> args, Collection<T> items, BindingRecyclerViewAdapter.ItemIds<T> itemIds) {
        setAdapter(recyclerView, args, items, BindingRecyclerViewAdapterFactory.DEFAULT, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "adapter", "itemIds"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, Collection<T> items, BindingRecyclerViewAdapterFactory factory, BindingRecyclerViewAdapter.ItemIds<T> itemIds) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(recyclerView, arg);
            adapter.setItemIds(itemIds);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    // AdapterView
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, null, null);
    }

    @BindingAdapter({"itemView", "items", "adapter"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingAdapterViewFactory factory) {
        setAdapter(adapterView, arg, items, factory, null, null);
    }

    @BindingAdapter({"itemView", "items", "dropDownItemView"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, ItemView dropDownItemView) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, dropDownItemView, null);
    }

    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, null, itemIds);
    }

    @BindingAdapter({"itemView", "items", "adapter", "dropDownItemView"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingAdapterViewFactory factory, ItemView dropDownItemView) {
        setAdapter(adapterView, arg, items, factory, dropDownItemView, null);
    }

    @BindingAdapter({"itemView", "items", "adapter", "itemIds"})
    public static <T> void setAdapter(AdapterView adapter, ItemViewArg<T> args, Collection<T> items, BindingAdapterViewFactory factory, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapter, args, items, factory, null, itemIds);
    }

    @BindingAdapter({"itemView", "items", "dropDownItemView", "itemIds"})
    public static <T> void setAdapter(AdapterView adapter, ItemViewArg<T> args, Collection<T> items, ItemView dropDownItemsView, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapter, args, items, BindingAdapterViewFactory.DEFAULT, dropDownItemsView, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "adapter", "dropDownItemView", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingAdapterViewFactory factory, ItemView dropDownItemView, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(adapterView, arg);
            adapterView.setAdapter(adapter);
        }
        adapter.setDropDownItemView(dropDownItemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    // ViewPager
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(viewPager, arg, items, BindingViewPagerAdapterFactory.DEFAULT, null);
    }

    @BindingAdapter({"itemView", "items", "adapter"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapterFactory factory) {
        setAdapter(viewPager, arg, items, factory, null);
    }

    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        setAdapter(viewPager, arg, items, BindingViewPagerAdapterFactory.DEFAULT, pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "adapter", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapterFactory factory, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = factory.create(viewPager, arg);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @BindingConversion
    public static ItemViewArg toItemViewArg(ItemView itemView) {
        return ItemViewArg.of(itemView);
    }

    @BindingConversion
    public static ItemViewArg toItemViewArg(ItemViewSelector<?> selector) {
        return ItemViewArg.of(selector);
    }

    @BindingConversion
    public static BindingAdapterViewFactory toAdapterViewAdapterFactory(String className) {
        return new ClassNameBindingCollectionAdapterFactories.ClassNameBindingAdapterViewFactory(className);
    }

    @BindingConversion
    public static BindingRecyclerViewAdapterFactory toRecyclerViewAdapterFactory(String className) {
        return new ClassNameBindingCollectionAdapterFactories.ClassNameBindingRecyclerViewFactory(className);
    }

    @BindingConversion
    public static BindingViewPagerAdapterFactory toViewPagerAdapterFactory(String className) {
        return new ClassNameBindingCollectionAdapterFactories.ClassNameBindingViewPagerFactory(className);
    }

    /**
     * Helper to throw an exception when {@link android.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     *
     * @deprecated will be removed.
     */
    @Deprecated
    public static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Utils.throwMissingVariable(binding, bindingVariable, layoutRes);
    }

    /**
     * Returns the name for the given binding variable int. Warning! This uses reflection so it
     * should <em>only</em> be used for debugging.
     *
     * @throws Resources.NotFoundException if the name cannot be found.
     * @deprecated will be removed.
     */
    @Deprecated
    public static String getBindingVariableName(Context context, int bindingVariable) throws Resources.NotFoundException {
        return Utils.getBindingVariableName(context, bindingVariable);
    }
}
