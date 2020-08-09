package me.tatarka.bindingcollectionadapter.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter.sample.databinding.NetworkStateItemErrorBinding
import me.tatarka.bindingcollectionadapter.sample.databinding.NetworkStateItemProgressBinding

class HeaderFooterLoadStateAdapter(
        private val retryCallback: () -> Unit
) : LoadStateAdapter<HeaderFooterLoadStateAdapter.BindingViewHolder>() {

    override fun onBindViewHolder(holder: BindingViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Error) {
            holder.binding.setVariable(BR.error, loadState)
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState
    ): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (loadState) {
            is LoadState.Error -> BindingViewHolder(NetworkStateItemErrorBinding.inflate(inflater, parent, false))
            else -> BindingViewHolder(NetworkStateItemProgressBinding.inflate(inflater, parent, false))
        }
    }

    override fun getStateViewType(loadState: LoadState): Int {
        return when (loadState) {
            is LoadState.Error -> R.layout.network_state_item_error
            else -> R.layout.network_state_item_progress
        }
    }

    class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}