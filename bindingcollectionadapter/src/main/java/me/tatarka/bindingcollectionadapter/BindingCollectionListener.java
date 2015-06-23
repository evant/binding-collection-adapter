package me.tatarka.bindingcollectionadapter;

import android.databinding.ViewDataBinding;

/**
 * Listens to when a binding is created or bound to an item in the collection. This is an
 * escape-hatch if you need to do something to the views that you'd normally do in the adapter and
 * cannot do through databinding.
 */
public interface BindingCollectionListener<T> {
    /**
     * Called when the binding is created. You may cast the binding to the implementation specific
     * to it's layout to get at the generated view fields.
     */
    void onBindingCreated(ViewDataBinding binding);

    /**
     * Called when the binding is bound to an item in in the collection. You may cast the binding to
     * the implementation specific to it's layout to get at the generated view fields. This is
     * called <em>after</em> the item is bound.
     */
    void onBindingBound(ViewDataBinding binding, int position, T item);
}
