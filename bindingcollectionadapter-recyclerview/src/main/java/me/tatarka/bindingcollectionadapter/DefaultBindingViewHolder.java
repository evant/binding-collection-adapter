package me.tatarka.bindingcollectionadapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class DefaultBindingViewHolder extends RecyclerView.ViewHolder implements BindingViewHolder {

    private final ViewDataBinding binding;

    public DefaultBindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }
}
