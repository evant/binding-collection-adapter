package me.tatarka.bindingcollectionadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import me.tatarka.bindingcollectionadapter.sample.databinding.ViewpagerViewBinding

class FragmentViewPagerView : Fragment() {
    private lateinit var viewModel: MutableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MutableViewModel::class.java)
        viewModel.setCheckable(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ViewpagerViewBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.listeners = PagerListeners(it, viewModel)
            it.executePendingBindings()

            it.tabs.setupWithViewPager(it.pager)
            it.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(it.tabs))
        }.root
    }

    private class PagerListeners(
        private val binding: ViewpagerViewBinding,
        private val delegate: Listeners
    ) : Listeners {

        override fun onAddItem() {
            delegate.onAddItem()
            updateTabs()
        }

        override fun onRemoveItem() {
            delegate.onRemoveItem()
            updateTabs()
        }

        private fun updateTabs() {
            // We can't use tabs.setTabsFromPagerAdapter() because it will reset the current item to 0.
            binding.tabs.removeAllTabs()
            val adapter = binding.pager.adapter!!
            for (i in 0 until adapter.count) {
                binding.tabs.addTab(
                    binding.tabs.newTab().setText(adapter.getPageTitle(i)),
                    i == binding.pager.currentItem
                )
            }
        }
    }
}
