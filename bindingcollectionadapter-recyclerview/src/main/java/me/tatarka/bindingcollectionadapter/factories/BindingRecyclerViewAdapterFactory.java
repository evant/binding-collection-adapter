package me.tatarka.bindingcollectionadapter.factories;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.BindingViewHolder;
import me.tatarka.bindingcollectionadapter.DefaultBindingViewHolder;
import me.tatarka.bindingcollectionadapter.ItemViewArg;

public interface BindingRecyclerViewAdapterFactory {
    <T, VH extends RecyclerView.ViewHolder & BindingViewHolder>
    BindingRecyclerViewAdapter<T, VH> create(RecyclerView recyclerView, ItemViewArg<T> arg);

    BindingRecyclerViewAdapterFactory DEFAULT = new BindingRecyclerViewAdapterFactory() {
        @Override
        public <T, VH extends RecyclerView.ViewHolder & BindingViewHolder> BindingRecyclerViewAdapter<T, VH>
        create(RecyclerView recyclerView, ItemViewArg<T> arg) {
            return new BindingRecyclerViewAdapter(arg) {
                @Override
                protected RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
                    return new DefaultBindingViewHolder(binding);
                }
            };
        }
    };
}
