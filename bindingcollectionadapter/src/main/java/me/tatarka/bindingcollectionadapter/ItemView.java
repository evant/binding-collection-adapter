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
     *
     * @see #setBindingVariable(int)
     * @see #setLayoutRes(int)
     */
    public static ItemView of(int bindingVariable, @LayoutRes int layoutRes) {
        return new ItemView()
                .setBindingVariable(bindingVariable)
                .setLayoutRes(layoutRes);
    }

    /**
     * Sets the binding variable. This is one of the {@code BR} constants that references the
     * variable tag in the item layout file.
     *
     * @return the {@code ItemView} for chaining
     */
    public ItemView setBindingVariable(int bindingVariable) {
        this.bindingVariable = bindingVariable;
        return this;
    }

    /**
     * Sets the layout resource of the item.
     *
     * @return the {@code ItemView} for chaining
     */
    public ItemView setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    /**
     * Set an arbitrary int value, for example {@link BindingListViewAdapter#ITEM_ID} or {@link
     * BindingListViewAdapter#DROP_DOWN_LAYOUT}.
     *
     * @return the {@code ItemView} for chaining
     */
    public ItemView set(String key, int value) {
        if (extras == null) {
            extras = new ArrayMap<>();
        }
        extras.put(key, value);
        return this;
    }

    /**
     * Set an arbitrary value, for example {@link BindingViewPagerAdapter#TITLE}.
     *
     * @return the {@code ItemView} for chaining
     */
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

    /**
     * Get an int value set with {@link #set(String, int)}, or 0 if it does not exist.
     */
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

    /**
     * Get a value set with {@link #set(String, Object)} or null if it does not exist.
     */
    public Object get(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }
}
