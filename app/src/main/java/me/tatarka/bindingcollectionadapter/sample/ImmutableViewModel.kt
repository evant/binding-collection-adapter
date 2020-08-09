package me.tatarka.bindingcollectionadapter.sample

import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.delay
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
        }, PAGE_SIZE).build()

    val pagedListV3: LiveData<PagingData<Any>> = Pager<Int, Any>(PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = 100,
            prefetchDistance = PAGE_SIZE * 2,
            enablePlaceholders = false)) {
        object : PagingSource<Int, Any>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
                val safeKey = params.key ?: 0
                val list =
                        (0 until params.loadSize).map {
                            ImmutableItem(
                                    index = it + safeKey,
                                    checked = false
                            )
                        }
                // Pretend we are slow
                delay(1000)

                return LoadResult.Page(
                        data = list,
                        prevKey = if (safeKey == 0) null else (safeKey - params.loadSize),
                        nextKey = if (safeKey >= TOTAL_COUNT - params.loadSize) null else (safeKey + params.loadSize),
                        itemsBefore = safeKey,
                        itemsAfter = TOTAL_COUNT - params.loadSize - safeKey
                )
            }
        }
    }.flow.asLiveData()

    val headerLoadStateAdapter = HeaderFooterLoadStateAdapter {
        //TODO retry
    }

    val footerItemBinding = OnItemBindClass<LoadState>().apply {
        map<LoadState.Error>(BR.item, R.layout.network_state_item_error)
        map<LoadState.Loading>(BR.item, R.layout.network_state_item_progress)
    }.toItemBinding()

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

    companion object {
        private val TOTAL_COUNT = 200
        private val PAGE_SIZE = 20
    }
}
