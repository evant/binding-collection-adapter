package me.tatarka.bindingcollectionadapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.lang.reflect.Field;
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
    public static <T> void setAdapter(AdapterView adapterView, ItemView itemView, Collection<T> items) {
        setAdapter(adapterView, itemView, items, null);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(adapterView, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = new BindingListViewAdapter<T>(itemView);
            adapterView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = new BindingListViewAdapter<T>(selector);
            adapterView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemView itemView, Collection<T> items) {
        setAdapter(adapterView, adapterClassName, itemView, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items) {
        setAdapter(adapterView, adapterClassName, selector, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemView itemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, itemView);
            adapterView.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewSelector<T> selector, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, selector);
            adapterView.setAdapter(adapter);
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
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        // Yeah reflection is slow, but this only happens when there is a programmer error.
        String bindingVariableName;
        try {
            bindingVariableName = getBindingVariableName(context, bindingVariable);
        } catch (Resources.NotFoundException e) {
            // Fall back to int
            bindingVariableName = "" + bindingVariable;
        }
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    /**
     * Returns the name for the given binding variable int. Warning! This uses reflection so it
     * should <em>only</em> be used for debugging.
     *
     * @throws Resources.NotFoundException if the name cannot be found.
     */
    public static String getBindingVariableName(Context context, int bindingVariable) throws Resources.NotFoundException {
        try {
            String packageName = context.getApplicationInfo().packageName;
            Class BRClass = Class.forName(packageName + ".BR");
            Field[] fields = BRClass.getFields();
            for (Field field : fields) {
                int value = field.getInt(null);
                if (value == bindingVariable) {
                    return field.getName();
                }
            }
        } catch (Exception e) {
            //Ignore, throws NotFoundException
        }
        throw new Resources.NotFoundException("" + bindingVariable);
    }
}
