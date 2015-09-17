package me.tatarka.bindingcollectionadapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper databinding utilities. May be made public some time in the future if they prove to be
 * useful.
 */
class Utils {
    private static final String TAG = "BCAdapters";

    /**
     * Helper to throw an exception when {@link android.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     */
    static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
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
    static String getBindingVariableName(Context context, int bindingVariable) throws Resources.NotFoundException {
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

    /**
     * Ensures the call was made on the main thread. This is enforced for all ObservableList change
     * operations.
     */
    static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }

    /**
     * Helper method for backwards compat with {@link BindingCollectionAdapter#setItems(Collection)}.
     * Will be removed sometime in the future.
     */
    static <T> List<T> getListFromCollection(Collection<T> collection) {
        if (collection instanceof List) {
            return (List<T>) collection;
        } else {
            Log.w(TAG, "collection: " + collection + " is not a List. Support for non-list collections will be removed in a future version");
            return new ArrayList<>(collection);
        }
    }
}
