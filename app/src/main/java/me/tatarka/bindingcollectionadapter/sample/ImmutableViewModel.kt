package me.tatarka.bindingcollectionadapter.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
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
