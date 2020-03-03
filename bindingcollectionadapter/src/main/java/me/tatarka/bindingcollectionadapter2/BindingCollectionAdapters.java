package me.tatarka.bindingcollectionadapter2;

import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.WrapperListAdapter;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.viewpager.widget.ViewPager;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    // AdapterView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "itemTypeCount", "items", "adapter", "itemDropDownLayout", "itemIds", "itemIsEnabled"}, requireAll = false)
    public static <T> void setAdapter(AdapterView adapterView, ItemBinding<? super T> itemBinding, Integer itemTypeCount, List items, BindingListViewAdapter<T> adapter, @LayoutRes int itemDropDownLayout, BindingListViewAdapter.ItemIds<? super T> itemIds, BindingListViewAdapter.ItemIsEnabled<? super T> itemIsEnabled) {
        if (itemBinding != null) {
            BindingListViewAdapter<T> oldAdapter = (BindingListViewAdapter<T>) unwrapAdapter(adapterView.getAdapter());
            if (adapter == null) {
                if (oldAdapter == null) {
                    int count = itemTypeCount != null ? itemTypeCount : 1;
                    adapter = new BindingListViewAdapter<>(count);
                } else {
                    adapter = oldAdapter;
                }
            }
            adapter.setItemBinding(itemBinding);
            adapter.setDropDownItemLayout(itemDropDownLayout);
            adapter.setItems(items);
            adapter.setItemIds(itemIds);
            adapter.setItemIsEnabled(itemIsEnabled);

            if (oldAdapter != adapter) {
                adapterView.setAdapter(adapter);
            }
        } else {
            adapterView.setAdapter(null);
        }
    }

    /**
     * Unwraps any {@link android.widget.WrapperListAdapter}, commonly {@link
     * android.widget.HeaderViewListAdapter}.
     */
    private static Adapter unwrapAdapter(Adapter adapter) {
        return adapter instanceof WrapperListAdapter
                ? unwrapAdapter(((WrapperListAdapter) adapter).getWrappedAdapter())
                : adapter;
    }

    // ViewPager
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinding", "items", "adapter", "pageTitles"}, requireAll = false)
    public static <T> void setAdapter(ViewPager viewPager, ItemBinding<? super T> itemBinding, List items, BindingViewPagerAdapter<T> adapter, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        if (itemBinding != null) {
            BindingViewPagerAdapter<T> oldAdapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
            if (adapter == null) {
                if (oldAdapter == null) {
                    adapter = new BindingViewPagerAdapter<>();
                } else {
                    adapter = oldAdapter;
                }
            }
            adapter.setItemBinding(itemBinding);
            adapter.setItems(items);
            adapter.setPageTitles(pageTitles);

            if (oldAdapter != adapter) {
                viewPager.setAdapter(adapter);
            }
        } else {
            viewPager.setAdapter(null);
        }
    }

    @BindingConversion
    public static <T> ItemBinding<T> toItemBinding(OnItemBind<T> onItemBind) {
        return ItemBinding.of(onItemBind);
    }
}
