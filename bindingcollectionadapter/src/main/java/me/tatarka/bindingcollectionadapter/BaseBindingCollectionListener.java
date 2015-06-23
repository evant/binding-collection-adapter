package me.tatarka.bindingcollectionadapter;

import android.databinding.ViewDataBinding;

/**
 * You may extend this instead of {@link BindingCollectionListener} if you do not need to implement
 * all methods.
 */
public abstract class BaseBindingCollectionListener<T> implements BindingCollectionListener<T> {
    @Override
    public void onBindingCreated(ViewDataBinding binding) {

    }

    @Override
    public void onBindingBound(ViewDataBinding binding, int position, T item) {

    }
}
