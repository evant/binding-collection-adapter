package me.tatarka.bindingcollectionadapter.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import me.tatarka.bindingcollectionadapter.sample.databinding.Viewpager2ViewBinding

class FragmentViewPager2View : Fragment() {
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
        return Viewpager2ViewBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.listeners = PagerListeners(viewModel)
            it.executePendingBindings()

            TabLayoutMediator(it.tabs, it.pager) { tab, position ->
                val item = viewModel.items[position]
                tab.text = viewModel.pageTitles.getPageTitle(position, item)
            }.attach()
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
