package me.tatarka.bindingcollectionadapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

public class DefaultBindingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T, DefaultBindingViewHolder> {

    public DefaultBindingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
        super(arg);
    }

    @Override
    protected DefaultBindingViewHolder createViewHolder(ViewDataBinding binding) {
        return new DefaultBindingViewHolder(binding);
    }
}
