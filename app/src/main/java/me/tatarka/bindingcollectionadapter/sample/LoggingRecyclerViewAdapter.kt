package me.tatarka.bindingcollectionadapter.sample

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

private const val TAG = "RecyclerView"

class LoggingRecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {

    override fun onCreateBinding(
        inflater: LayoutInflater, @LayoutRes layoutId: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding {
        return super.onCreateBinding(inflater, layoutId, viewGroup).apply {
            Log.d(TAG, "created binding: $this")
        }
    }

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int, @LayoutRes layoutRes: Int,
        position: Int,
        item: T
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        Log.d(TAG, "bound binding: $binding at position: $position")
    }
}
