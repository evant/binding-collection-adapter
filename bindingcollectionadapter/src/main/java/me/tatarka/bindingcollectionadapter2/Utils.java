package me.tatarka.bindingcollectionadapter2;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Helper databinding utilities. May be made public some time in the future if they prove to be
 * useful.
 */
class Utils {
    private static final String TAG = "BCAdapters";

    @Nullable
    private static Field lifecycleOwnerField;
    private static boolean fieldFaild;

    /**
     * Helper to throw an exception when {@link android.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     */
    static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        String bindingVariableName = DataBindingUtil.convertBrIdToString(bindingVariable);
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    /**
     * Returns the lifecycle owner associated with the given view. This currently requires the view
     * to use databinding and uses reflection. This will hopefully be replaced with a better
     * implementation once https://issuetracker.google.com/issues/112929938 gets implemented.
     */
    @Nullable
    @MainThread
    static LifecycleOwner findLifecycleOwner(View view) {
        ViewDataBinding binding = DataBindingUtil.findBinding(view);
        if (binding == null) {
            return null;
        }
        return getLifecycleOwner(binding);
    }

    /**
     * Returns the lifecycle owner from a {@code ViewDataBinding} using reflection.
     */
    @Nullable
    @MainThread
    private static LifecycleOwner getLifecycleOwner(ViewDataBinding binding) {
        if (!fieldFaild && lifecycleOwnerField == null) {
            try {
                lifecycleOwnerField = ViewDataBinding.class.getDeclaredField("mLifecycleOwner");
                lifecycleOwnerField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                fieldFaild = true;
                return null;
            }
        }
        if (lifecycleOwnerField == null) {
            return null;
        }
        try {
            return (LifecycleOwner) lifecycleOwnerField.get(binding);
        } catch (IllegalAccessException e) {
            return null;
        }
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
     * Constructs a binding adapter class from it's class name using reflection.
     */
    @SuppressWarnings("unchecked")
    static <T, A extends BindingCollectionAdapter<T>> A createClass(Class<? extends BindingCollectionAdapter> adapterClass, ItemBinding<T> itemBinding) {
        try {
            return (A) adapterClass.getConstructor(ItemBinding.class).newInstance(itemBinding);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }
}
