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

    private ArrayMap<String, Integer> extraLayouts;

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
     * A convenience method for {@code ItemView.setBindingVariable(int).setLayoutRes(int)}.
     *
     * @return the {@code ItemView} for chaining
     */
    public ItemView set(int bindingVariable, @LayoutRes int layoutRes) {
        this.bindingVariable = bindingVariable;
        this.layoutRes = layoutRes;
        return this;
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
     * Set an additional layout res value with the given key. This is used for adapters that want to
     * show the same content in multiple ways.
     *
     * @return the {@code ItemView} for chaining
     * @see BindingListViewAdapter#DROP_DOWN_LAYOUT
     */
    public ItemView setLayoutRes(String key, @LayoutRes int layoutRes) {
        if (extraLayouts == null) {
            extraLayouts = new ArrayMap<>();
        }
        extraLayouts.put(key, layoutRes);
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
     * Get an additional layout res set with {@link #setLayoutRes(String, int)}, or 0 if it doesn't
     * exist.
     */
    @LayoutRes
    public int getLayoutRes(String key) {
        if (extraLayouts == null) {
            return 0;
        }
        Integer value = extraLayouts.get(key);
        if (value == null) {
            return 0;
        }
        return value;
    }
}
