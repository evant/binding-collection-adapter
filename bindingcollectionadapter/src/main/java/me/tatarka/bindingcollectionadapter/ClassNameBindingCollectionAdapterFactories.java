package me.tatarka.bindingcollectionadapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AdapterView;

import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;
import me.tatarka.bindingcollectionadapter.factories.BindingViewPagerAdapterFactory;

/**
 * Constructs a {@link BindingCollectionAdapter} based on the given class name through reflection.
 * The class to construct <em>must</em> have a public constructor that takes an {@link
 * ItemViewArg}.
 */
public class ClassNameBindingCollectionAdapterFactories {
    private static final String TAG = "BCAdapters";

    public static class ClassNameBindingAdapterViewFactory implements BindingAdapterViewFactory {
        private String className;

        public ClassNameBindingAdapterViewFactory(String className) {
            this.className = className;
        }

        @Override
        public <T> BindingListViewAdapter<T> create(AdapterView adapterView, ItemViewArg<T> arg) {
            return createClass(className, arg);
        }
    }

    public static class ClassNameBindingRecyclerViewFactory implements BindingRecyclerViewAdapterFactory {
        private String className;

        public ClassNameBindingRecyclerViewFactory(String className) {
            this.className = className;
        }

        @Override
        public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
            return createClass(className, arg);
        }
    }

    public static class ClassNameBindingViewPagerFactory implements BindingViewPagerAdapterFactory {
        private String className;

        public ClassNameBindingViewPagerFactory(String className) {
            this.className = className;
        }

        @Override
        public <T> BindingViewPagerAdapter<T> create(ViewPager viewPager, ItemViewArg<T> arg) {
            return createClass(className, arg);
        }
    }

    @SuppressWarnings("unchecked")
    static <T, A extends BindingCollectionAdapter<T>> A createClass(String className, ItemViewArg<T> arg) {
        try {
            return (A) Class.forName(className).getConstructor(ItemViewArg.class).newInstance(arg);
        } catch (Exception e) {
            // Fallback to either an ItemView or ItemViewSelector constructor.
            Log.w(TAG, "Could not find constructor " + className + "(ItemViewArg), falling back to either ItemView or ItemViewSelector constructor. You should create this single unified constructor as this fallback will be removed in a later version.");
            try {
                if (arg.selector == BaseItemViewSelector.empty()) {
                    return (A) Class.forName(className).getConstructor(ItemView.class).newInstance(arg.itemView);
                } else {
                    return (A) Class.forName(className).getConstructor(ItemViewSelector.class).newInstance(arg.selector);
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
