package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by evan on 6/30/15.
 */
public class LoggingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
    public static final String TAG = "RecyclerView";

    public LoggingRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
        super(arg);
    }

    public LoggingRecyclerViewAdapter(@NonNull ItemView itemView) {
        super(itemView);
    }

    public LoggingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
        super(selector);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        Log.d(TAG, "created binding: " + binding);
        return binding;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {
        super.onBindBinding(binding, bindingVariable, layoutRes, position, item);
        Log.d(TAG, "bound binding: " + binding + " at position: " + position);
    }
}
