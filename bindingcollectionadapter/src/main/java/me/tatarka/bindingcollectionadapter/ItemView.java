package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;

/**
 * An {@code ItemView} provides the necessary information for an item in a collection view. All
 * views require a binding variable and layout resource, though some may require additional
 * information which can be set with arbitrary String keys. This class is explicitly mutable so that
 * only one instance is required and set multiple times in a {@link ItemViewSelector} when multiple
 * item types are needed.
 */
public final class ItemView {
    int bindingVariable;
    @LayoutRes
    int layoutRes;

    private ArrayMap<String, Object> extras;

    /**
     * Constructs a new {@code ItemView} with the given binding variable and layout res.
     */
    public static ItemView of(int bindingVariable, @LayoutRes int layoutRes) {
        return new ItemView()
                .setBindingVariable(bindingVariable)
                .setLayoutRes(layoutRes);
    }

    public ItemView setBindingVariable(int bindingVariable) {
        this.bindingVariable = bindingVariable;
        return this;
    }


    public ItemView setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    public ItemView set(String key, int value) {
        if (extras == null) {
            extras = new ArrayMap<>();
        }
        extras.put(key, value);
        return this;
    }

    public ItemView set(String key, Object value) {
        if (extras == null) {
            extras = new ArrayMap<>();
        }
        extras.put(key, value);
        return this;
    }

    public int getBindingVariable() {
        return bindingVariable;
    }
   
    @LayoutRes
    public int getLayoutRes() {
        return layoutRes;
    }

    public int getInt(String key) {
        if (extras == null) {
            return 0;
        }
        Object value = extras.get(key);
        if (value == null) {
            return 0;
        }
        return (int) value;
    }

    public Object get(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }
}
