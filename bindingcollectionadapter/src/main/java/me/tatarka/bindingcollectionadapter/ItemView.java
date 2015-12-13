package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;

/**
 * An {@code ItemView} provides the necessary information for an item in a collection view. All
 * views require a binding variable and layout resource, though some may require additional
 * information which can be set with arbitrary String keys. This class is explicitly mutable so that
 * only one instance is required and set multiple times in a {@link ItemViewSelector} when multiple
 * item types are needed.
 */
public final class ItemView {
    /**
     * Use this constant as the {@code bindingVariable} to not bind any variable to the layout. This
     * is useful if no data is needed for that layout, like a static footer or loading indicator for
     * example.
     */
    public static final int BINDING_VARIABLE_NONE = 0;

    private int bindingVariable;
    @LayoutRes
    private int layoutRes;

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

    public int bindingVariable() {
        return bindingVariable;
    }

    @LayoutRes
    public int layoutRes() {
        return layoutRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemView itemView = (ItemView) o;

        if (bindingVariable != itemView.bindingVariable) return false;
        return layoutRes == itemView.layoutRes;
    }

    @Override
    public int hashCode() {
        int result = bindingVariable;
        result = 31 * result + layoutRes;
        return result;
    }
}
