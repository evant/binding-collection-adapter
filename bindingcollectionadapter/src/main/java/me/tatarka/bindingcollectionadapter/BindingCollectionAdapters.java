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

import java.lang.reflect.Field;
import java.util.Collection;

import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingViewPagerAdapterFactory;

/**
 * All the BindingAdapters so that you can set your adapters and items directly in your layout.
 */
public class BindingCollectionAdapters {
    private static final String TAG = "BCAdapters";

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(recyclerView, BindingRecyclerViewAdapterFactory.DEFAULT, arg, items);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(recyclerView, ItemViewArg.of(arg), items);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, BindingRecyclerViewAdapterFactory factory, ItemViewArg<T> arg, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(recyclerView, arg);
            recyclerView.setAdapter(adapter);
        }
        adapter.setItems(items);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(RecyclerView recyclerView, BindingRecyclerViewAdapterFactory factory, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(recyclerView, factory, ItemViewArg.of(arg), items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(adapterView, arg, items, null);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(adapterView, ItemViewArg.of(arg), items, null);
    }

    @BindingAdapter({"itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, arg, dropDownItemView, items, null);
    }

    @BindingAdapter({"itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, ItemViewArg.of(arg), dropDownItemView, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, arg, null, items, itemIds);
    }

    @BindingAdapter({"itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, ItemViewArg.of(arg), null, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, BindingAdapterViewFactory.DEFAULT, arg, dropDownItemView, items, itemIds);
    }

    @BindingAdapter({"itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, ItemViewSelector<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, ItemViewArg.of(arg), dropDownItemView, items, itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(adapterView, factory, arg, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(adapterView, factory, ItemViewArg.of(arg), items, null);
    }

    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, factory, arg, dropDownItemView, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewSelector<T> arg, ItemView dropDownItemView, Collection<T> items) {
        setAdapter(adapterView, factory, ItemViewArg.of(arg), dropDownItemView, items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewArg<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, factory, arg, null, items, itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewSelector<T> arg, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, factory, ItemViewArg.of(arg), null, items, itemIds);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewArg<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        BindingListViewAdapter<T> adapter = (BindingListViewAdapter<T>) adapterView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(adapterView, arg);
            adapterView.setAdapter(adapter);
        }
        adapter.setDropDownItemView(dropDownItemView);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
    }

    @BindingAdapter({"adapter", "itemView", "dropDownItemView", "items", "itemIds"})
    public static <T> void setAdapter(AdapterView adapterView, BindingAdapterViewFactory factory, ItemViewSelector<T> arg, ItemView dropDownItemView, Collection<T> items, BindingListViewAdapter.ItemIds<T> itemIds) {
        setAdapter(adapterView, factory, ItemViewArg.of(arg), dropDownItemView, items, itemIds);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(viewPager, arg, items, null);
    }

    @BindingAdapter({"itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(viewPager, ItemViewArg.of(arg), items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        setAdapter(viewPager, BindingViewPagerAdapterFactory.DEFAULT, arg, items, pageTitles);
    }

    @BindingAdapter({"itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, ItemViewSelector<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        setAdapter(viewPager, ItemViewArg.of(arg), items, pageTitles);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, BindingViewPagerAdapterFactory factory, ItemViewArg<T> arg, Collection<T> items) {
        setAdapter(viewPager, factory, arg, items, null);
    }

    @BindingAdapter({"adapter", "itemView", "items"})
    public static <T> void setAdapter(ViewPager viewPager, BindingViewPagerAdapterFactory factory, ItemViewSelector<T> arg, Collection<T> items) {
        setAdapter(viewPager, factory, ItemViewArg.of(arg), items, null);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, BindingViewPagerAdapterFactory factory, ItemViewArg<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        BindingViewPagerAdapter<T> adapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
        if (adapter == null) {
            adapter = factory.create(viewPager, arg);
            viewPager.setAdapter(adapter);
        }
        adapter.setItems(items);
        adapter.setPageTitles(pageTitles);
    }

    @BindingAdapter({"adapter", "itemView", "items", "pageTitles"})
    public static <T> void setAdapter(ViewPager viewPager, BindingViewPagerAdapterFactory factory, ItemViewSelector<T> arg, Collection<T> items, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
        setAdapter(viewPager, factory, ItemViewArg.of(arg), items, pageTitles);
    }

    @BindingConversion
    public static ItemViewArg toItemViewArg(ItemView itemView) {
        return ItemViewArg.of(itemView);
    }

    //This conversion does not work due to https://code.google.com/p/android/issues/detail?id=183651
    //Instead, we will have to create more @BindingAdapter overloads for ItemViewSelector
    //@BindingConversion
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
     * @deprecated You should use an explicit {@code Binding*AdapterFactory} instead. If you want to
     * still use a class name, use one of {@link ClassNameBindingCollectionAdapterFactories}.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T extends BindingCollectionAdapter> T createAdapter(String className, ItemViewArg<?> arg) {
        return (T) ClassNameBindingCollectionAdapterFactories.createClass(className, arg);
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
