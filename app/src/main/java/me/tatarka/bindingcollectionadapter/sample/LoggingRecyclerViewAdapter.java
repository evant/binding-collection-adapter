package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.util.Log;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by evan on 6/30/15.
 */
public class LoggingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {
    public static final String TAG = "RecyclerView";
    
    public LoggingRecyclerViewAdapter(@NonNull ItemView itemView) {
        super(itemView);
    }

    public LoggingRecyclerViewAdapter(@NonNull ItemViewSelector<T> selector) {
        super(selector);
    }

    @Override
    public void onBindingCreated(ViewDataBinding binding) {
        Log.d(TAG, "created binding: " + binding);
    }

    @Override
    public void onBindingBound(ViewDataBinding binding, int position, T item) {
        Log.d(TAG, "bound binding: " + binding + " at position: " + position);
    }
}
