package me.tatarka.bindingcollectionadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import me.tatarka.bindingcollectionadapter.sample.databinding.PagedRecyclerViewBinding

class FragmentPagedRecyclerView : Fragment() {
    private lateinit var viewModel: ImmutableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return PagedRecyclerViewBinding.inflate(inflater, container, false).also {
            it.setLifecycleOwner(this)
            it.viewModel = viewModel
            it.listeners = viewModel
            it.executePendingBindings()
        }.root
    }
}