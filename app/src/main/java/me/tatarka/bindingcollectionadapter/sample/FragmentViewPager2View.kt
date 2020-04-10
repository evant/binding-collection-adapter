package me.tatarka.bindingcollectionadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.tatarka.bindingcollectionadapter.sample.databinding.Viewpager2ViewBinding
import me.tatarka.bindingcollectionadapter.sample.databinding.ViewpagerViewBinding

class FragmentViewPager2View : Fragment() {
    private lateinit var viewModel: MutableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get()
        viewModel.setCheckable(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return Viewpager2ViewBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.listeners = PagerListeners(viewModel)
            it.executePendingBindings()

            TabLayoutMediator(it.tabs, it.pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                val item = viewModel.items[position]
                tab.text = viewModel.pageTitles.getPageTitle(position, item)
            }).attach()
        }.root
    }

    private class PagerListeners(
        private val delegate: Listeners
    ) : Listeners {

        override fun onAddItem() {
            delegate.onAddItem()
        }

        override fun onRemoveItem() {
            delegate.onRemoveItem()
        }
    }
}
