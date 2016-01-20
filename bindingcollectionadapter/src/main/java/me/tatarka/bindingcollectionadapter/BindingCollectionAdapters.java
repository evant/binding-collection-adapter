package me.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v4.view.ViewPager;
import android.widget.AdapterView;

import java.util.List;

import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingViewPagerAdapterFactory;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    // AdapterView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemView", "items", "adapter", "dropDownItemView", "itemIds", "itemIsEnabled"}, requireAll = false)
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, List<T> items, BindingAdapterViewFactory factory, ItemView dropDownItemView, BindingListViewAdapter.ItemIds<T> itemIds, BindingListViewAdapter.ItemIsEnabled<T> itemIsEnabled) {
        if (arg == null) {
            throw new IllegalArgumentException("itemView must not be null");
        }
        if (factory == null) {
            factory = BindingAdapterViewFactory.DEFAULT;
        }
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(adapterView, arg);
            adapterView.setAdapter(adapter);
        }
        adapter.setDropDownItemView(dropDownItemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setItemIsEnabled(itemIsEnabled);
    }

    // ViewPager
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemView", "items", "adapter", "pageTitles"}, requireAll = false)
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, List<T> items, BindingViewPagerAdapterFactory factory, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        if (arg == null) {
            throw new IllegalArgumentException("itemView must not be null");
        }
        if (factory == null) {
            factory = BindingViewPagerAdapterFactory.DEFAULT;
        }
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
