package me.tatarka.bindingcollectionadapter.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.DiffUtil
import me.tatarka.bindingcollectionadapter2.itemBindingOf
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import me.tatarka.bindingcollectionadapter2.map
import me.tatarka.bindingcollectionadapter2.toItemBinding
import java.util.*

class ImmutableViewModel : ViewModel(), ImmutableListeners {

    private val mutList = MutableLiveData<List<ImmutableItem>>().apply {
        value = (0 until 3).map { i -> ImmutableItem(index = i, checked = false) }
    }
    private val headerFooterList =
        Transformations.map<List<ImmutableItem>, List<Any>>(mutList) { input ->
            val list = ArrayList<Any>(input.size + 2)
            list.add("Header")
            list.addAll(input)
            list.add("Footer")
            list
        }
    val list: LiveData<List<Any>> = headerFooterList

    val pagedList: LiveData<PagedList<Any>> =
        LivePagedListBuilder(object : DataSource.Factory<Int, Any>() {
            override fun create(): DataSource<Int, Any> =
                object : PositionalDataSource<Any>() {

                    override fun loadInitial(
                        params: LoadInitialParams,
                        callback: LoadInitialCallback<Any>
                    ) {
                        val list =
                            (0 until params.requestedLoadSize).map {
                                ImmutableItem(
                                    index = it + params.requestedStartPosition,
                                    checked = false
                                )
                            }
                        // Pretend we are slow
                        Thread.sleep(1000)
                        callback.onResult(list, params.requestedStartPosition, 200)
                    }

                    override fun loadRange(
                        params: LoadRangeParams,
                        callback: LoadRangeCallback<Any>
                    ) {
                        val list =
                            (0 until params.loadSize).map {
                                ImmutableItem(
                                    index = it + params.startPosition,
                                    checked = false
                                )
                            }
                        // Pretend we are slow
                        Thread.sleep(1000)
                        callback.onResult(list)
                    }
                }
        }, 20).build()

    val items = itemBindingOf<Any>(BR.item, R.layout.item_immutable)

    val multipleItems = OnItemBindClass<Any>().apply {
        map<String>(BR.item, R.layout.item_header_footer)
        map<ImmutableItem>(BR.item, R.layout.item_immutable)
    }.toItemBinding().bindExtra(BR.listeners, this)

    val diff: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is ImmutableItem && newItem is ImmutableItem) {
                oldItem.index == newItem.index
            } else oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }

    override fun onToggleChecked(index: Int): Boolean {
        mutList.value = mutList.value!!.replaceAt(index) { item ->
            item.copy(checked = !item.checked)
        }
        return true
    }

    override fun onAddItem() {
        mutList.value = mutList.value!!.let { list ->
            list + ImmutableItem(index = list.size, checked = false)
        }
    }

    override fun onRemoveItem() {
        mutList.value!!.let { list ->
            if (list.size > 1) {
                mutList.value = list.dropLast(1)
            }
        }
    }
}
