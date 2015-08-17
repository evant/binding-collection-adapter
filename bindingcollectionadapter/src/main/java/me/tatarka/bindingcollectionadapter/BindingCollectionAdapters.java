package me.tatarka.bindingcollectionadapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AdapterView;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    private static final String TAG = "BCAdapters";

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new BindingRecyclerViewAdapter<>(arg);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, String adapterClassName, ItemViewArg<T> arg, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, arg);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(adapterView, arg, items, null);
    }

    @BindingAdapter({"itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, arg, dropDownItemView, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, arg, null, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = new BindingListViewAdapter<>(arg);
            adapterView.setAdapter(adapter);
        }
        adapter.setDropDownItemView(dropDownItemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(adapterView, adapterClassName, arg, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, adapterClassName, arg, dropDownItemView, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewArg<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, adapterClassName, arg, null, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, String adapterClassName, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, arg);
            adapterView.setAdapter(adapter);
        }
        adapter.setDropDownItemView(dropDownItemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(viewPager, arg, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = new BindingViewPagerAdapter<>(arg);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(viewPager, adapterClassName, arg, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, String adapterClassName, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = createAdapter(adapterClassName, arg);
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

    public static <T extends BindingCollectionAdapter> T createAdapter(String className, ItemViewArg<?> arg) {
        try {
            return (T) Class.forName(className).getConstructor(ItemViewArg.class).newInstance(arg);
        } catch (Exception e) {
            // Fallback to either an ItemView or ItemViewSelector constructor.
            Log.w(TAG, "Could not find constructor " + className + "(ItemViewArg), falling back to either ItemView or ItemViewSelector constructor. You should create this single unified constructor as this fallback will be removed in a later version.");
            try {
                if (arg.selector == BaseItemViewSelector.empty()) {
                    return (T) Class.forName(className).getConstructor(ItemView.class).newInstance(arg.itemView);
                } else {
                    return (T) Class.forName(className).getConstructor(ItemViewSelector.class).newInstance(arg.selector);
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    /**
     * @deprecated Use {@link #createAdapter(String, ItemViewArg)} instead.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T extends BindingCollectionAdapter> T createAdapter(String className, ItemView itemView) {
        try {
            return (T) Class.forName(className).getConstructor(ItemView.class).newInstance(itemView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @deprecated Use {@link #createAdapter(String, ItemViewArg)} instead.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
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
