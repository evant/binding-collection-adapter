package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v4.view.ViewPager;
import android.widget.AdapterView;
import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingViewPagerAdapterFactory;

import java.util.List;

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

    // AdapterView
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, null, null);
    }

    @BindingAdapter({"itemView", "items", "adapter"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, BindingAdapterViewFactory factory) {
        setAdapter(adapterView, arg, items, factory, null, null);
    }

    @BindingAdapter({"itemView", "items", "dropDownItemView"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, ItemView dropDownItemView) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, dropDownItemView, null);
    }

    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, arg, items, BindingAdapterViewFactory.DEFAULT, null, itemIds);
    }

    @BindingAdapter({"itemView", "items", "adapter", "dropDownItemView"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, BindingAdapterViewFactory factory, ItemView dropDownItemView) {
        setAdapter(adapterView, arg, items, factory, dropDownItemView, null);
    }

    @BindingAdapter({"itemView", "items", "adapter", "itemIds"})
    public static <T> void setAdapter(AdapterView adapter, ItemViewArg<T> args, List<T> items, BindingAdapterViewFactory factory, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapter, args, items, factory, null, itemIds);
    }

    @BindingAdapter({"itemView", "items", "dropDownItemView", "itemIds"})
    public static <T> void setAdapter(AdapterView adapter, ItemViewArg<T> args, List<T> items, ItemView dropDownItemsView, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapter, args, items, BindingAdapterViewFactory.DEFAULT, dropDownItemsView, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "adapter", "dropDownItemView", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, BindingAdapterViewFactory factory, ItemView dropDownItemView, BindingListViewAdapter.ItemIds<T> itemIds) {
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
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, List<T> items) {
        setAdapter(viewPager, arg, items, BindingViewPagerAdapterFactory.DEFAULT, null);
    }

    @BindingAdapter({"itemView", "items", "adapter"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, List<T> items, BindingViewPagerAdapterFactory factory) {
        setAdapter(viewPager, arg, items, factory, null);
    }

    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, List<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        setAdapter(viewPager, arg, items, BindingViewPagerAdapterFactory.DEFAULT, pageTitles);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "adapter", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, List<T> items, BindingViewPagerAdapterFactory factory, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
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
    public static BindingAdapterViewFactory toAdapterViewAdapterFactory(final String className) {
        return new BindingAdapterViewFactory() {
            @Override
            public <T> BindingListViewAdapter<T> create(AdapterView adapterView, ItemViewArg<T> arg) {
                return Utils.createClass(className, arg);
            }
        };
    }

    @BindingConversion
    public static BindingViewPagerAdapterFactory toViewPagerAdapterFactory(final String className) {
        return new BindingViewPagerAdapterFactory() {
            @Override
            public <T> BindingViewPagerAdapter<T> create(ViewPager viewPager, ItemViewArg<T> arg) {
                return Utils.createClass(className, arg);
            }
        };
    }
}
