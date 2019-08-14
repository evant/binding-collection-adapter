package me.tatarka.bindingcollectionadapter2;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Looper;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.LifecycleOwner;

/**
 * Helper databinding utilities. May be made public some time in the future if they prove to be
 * useful.
 */
class Utils {
    private static final String TAG = "BCAdapters";

    /**
     * Helper to throw an exception when {@link androidx.databinding.ViewDataBinding#setVariable(int,
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
     * Returns the lifecycle owner associated with the given view. Tries to get lifecycle owner first
     * from ViewDataBinding, else from View Context if view is not data-bound.
     */
    @Nullable
    @MainThread
    static LifecycleOwner findLifecycleOwner(View view) {
        ViewDataBinding binding = DataBindingUtil.findBinding(view);
        LifecycleOwner lifecycleOwner = null;
        if (binding != null) {
            lifecycleOwner = binding.getLifecycleOwner();
        }
        Context ctx = view.getContext();
        if (lifecycleOwner == null && ctx instanceof LifecycleOwner) {
            lifecycleOwner = (LifecycleOwner) ctx;
        }
        return lifecycleOwner;
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

}
