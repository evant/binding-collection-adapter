package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemBinding;

/**
 * Created by evan on 6/30/15.
 */
public class LoggingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
    public static final String TAG = "RecyclerView";

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        Log.d(TAG, "created binding: " + binding);
        return binding;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
        Log.d(TAG, "bound binding: " + binding + " at position: " + position);
    }
}
