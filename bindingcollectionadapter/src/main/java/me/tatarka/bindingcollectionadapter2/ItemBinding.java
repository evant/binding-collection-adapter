package me.tatarka.bindingcollectionadapter2;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;

/**
 * Provides the necessary information to bind an item in a collection to a view. This includes the
 * variable id and the layout as well as any extra bindings you may want to provide.
 *
 * @param <T> The item type.
 */
public final class ItemBinding<T> {

    /**
     * Use this constant as the variable id to not bind the item in the collection to the layout if
     * no data is need, like a static footer or loading indicator.
     */
    public static final int VAR_NONE = 0;
    private static final int VAR_INVALID = -1;
    private static final int LAYOUT_NONE = 0;

    /**
     * Constructs an instance with the given variable id and layout.
     */
    public static <T> ItemBinding<T> of(int variableId, @LayoutRes int layoutRes) {
        return new ItemBinding<T>(null).set(variableId, layoutRes);
    }

    /**
     * Constructs an instance with the given callback. It will be called for each item in the
     * collection to set the binding info.
     *
     * @see OnItemBind
     */
    public static <T> ItemBinding<T> of(OnItemBind<T> onItemBind) {
        if (onItemBind == null) {
            throw new NullPointerException("onItemBind == null");
        }
        return new ItemBinding<>(onItemBind);
    }

    private final OnItemBind<T> onItemBind;
    private int variableId;
    private int defaultVariableId = VAR_INVALID;
    @LayoutRes
    private int layoutRes;
    @LayoutRes
    private int defaultLayoutRes = LAYOUT_NONE;
    private SparseArray<Object> extraBindings;
    private SparseArray<Object> tempExtraBindings;

    private ItemBinding(OnItemBind<T> onItemBind) {
        this.onItemBind = onItemBind;
    }

    /**
     * Set the variable id and layout.
     */
    public final ItemBinding<T> set(int variableId, @LayoutRes int layoutRes) {
        return variableId(variableId).layoutRes(layoutRes);
    }

    /**
     * Set the variable id.
     */
    public final ItemBinding<T> variableId(int variableId) {
        this.variableId = defaultVariableId = variableId;
        return this;
    }

    /**
     * Set the layout.
     */
    public final ItemBinding<T> layoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = defaultLayoutRes = layoutRes;
        return this;
    }

    /**
     * Set the variable id and layout. This is normally called in {@link
     * OnItemBind#onItemBind(ItemBinding, int, Object)}.
     */
    public final ItemBinding<T> reset(int variableId, @LayoutRes int layoutRes) {
        return resetVariableId(variableId).resetLayoutRes(layoutRes);
    }

    /**
     * Set the variable id. This is normally called in {@link OnItemBind#onItemBind(ItemBinding,
     * int, Object)}.
     */
    public final ItemBinding<T> resetVariableId(int variableId) {
        this.variableId = variableId;
        return this;
    }

    /**
     * Set the layout. This is normally called in {@link OnItemBind#onItemBind(ItemBinding, int,
     * Object)}.
     */
    public final ItemBinding<T> resetLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    /**
     * Bind an extra variable to the view with the given variable id. The same instance will be
     * provided to all views the binding is bound to.
     */
    public final ItemBinding<T> bindExtra(int variableId, Object value) {
        if (extraBindings == null) {
            extraBindings = new SparseArray<>(1);
        }
        extraBindings.put(variableId, value);
        return this;
    }

    /**
     * Bind an extra variable to the view with the given variable id. This is normally
     * called in {@link OnItemBind#onItemBind(ItemBinding, int, Object)}.
     */
    public final ItemBinding<T> rebindExtra(int variableId, Object value) {
        if (tempExtraBindings == null) {
            tempExtraBindings = new SparseArray<>(1);
        }
        tempExtraBindings.put(variableId, value);
        return this;
    }

    /**
     * Clear extra variables.
     */
    public final ItemBinding<T> clearExtras() {
        extraBindings = null;
        return this;
    }

    /**
     * Remove an extra variable with the given variable id.
     */
    public ItemBinding<T> removeExtra(int variableId) {
        if (extraBindings != null) {
            extraBindings.remove(variableId);
        }
        return this;
    }

    /**
     * Returns the current variable id of this binding.
     */
    public final int variableId() {
        return variableId;
    }

    /**
     * Returns the current layout fo this binding.
     */
    @LayoutRes
    public final int layoutRes() {
        return layoutRes;
    }

    /**
     * Returns the current extra binding for the given variable id or null if one isn't present.
     */
    public final Object extraBinding(int variableId) {
        if (extraBindings == null) {
            return null;
        }
        return extraBindings.get(variableId);
    }

    /**
     * Updates the state of the binding for the given item and position. This is called internally
     * by the binding collection adapters.
     */
    public void onItemBind(int position, T item) {
        if (onItemBind != null) {
            variableId = defaultVariableId;
            layoutRes = defaultLayoutRes;
            tempExtraBindings = null;
            onItemBind.onItemBind(this, position, item);
            if (variableId == VAR_INVALID) {
                throw new IllegalStateException("variableId not set in onItemBind()");
            }
            if (layoutRes == LAYOUT_NONE) {
                throw new IllegalStateException("layoutRes not set in onItemBind()");
            }
        }
    }

    /**
     * Binds the item and extra bindings to the given binding. Returns true if anything was bound
     * and false otherwise. This is called internally by the binding collection adapters.
     *
     * @throws IllegalStateException if the variable id isn't present in the layout.
     */
    public boolean bind(ViewDataBinding binding, T item) {
        if (variableId == VAR_NONE) {
            return false;
        }
        boolean result = binding.setVariable(variableId, item);
        if (!result) {
            Utils.throwMissingVariable(binding, variableId, layoutRes);
        }
        applyExtraBindings(binding, extraBindings);
        applyExtraBindings(binding, tempExtraBindings);
        return true;
    }

    private void applyExtraBindings(ViewDataBinding binding, SparseArray<Object> extraBindings) {
        if (extraBindings != null) {
            for (int i = 0, size = extraBindings.size(); i < size; i++) {
                int variableId = extraBindings.keyAt(i);
                Object value = extraBindings.valueAt(i);
                if (variableId != VAR_NONE) {
                    binding.setVariable(variableId, value);
                }
            }
        }
    }
}
