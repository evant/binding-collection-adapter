package com.android.example.paging.pagingwithnetwork.reddit.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

//class PostsLoadStateAdapter(
//        private val adapter: PostsAdapter
//) : LoadStateAdapter<NetworkStateItemViewHolder>() {
//    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
//        holder.bindTo(loadState)
//    }
//
//    override fun onCreateViewHolder(
//            parent: ViewGroup,
//            loadState: LoadState
//    ): NetworkStateItemViewHolder {
//        return NetworkStateItemViewHolder(parent) { adapter.retry() }
//    }
//}