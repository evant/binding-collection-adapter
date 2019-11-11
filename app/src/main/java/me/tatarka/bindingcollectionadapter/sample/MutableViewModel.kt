package me.tatarka.bindingcollectionadapter.sample

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.*
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class MutableViewModel : ViewModel(), Listeners {

    val items = ObservableArrayList<MutableItem>().apply {
        for (i in 0 until 3) {
            add(MutableItem(i))
        }
    }
    private var checkable = false

    /**
     * Items merged with a header on top and footer on bottom.
     */
    val headerFooterItems = MergeObservableList<Any>()
        .insertItem("Header")
        .insertList(items)
        .insertItem("Footer")

    /**
     * Custom adapter that logs calls.
     */
    val adapter = LoggingRecyclerViewAdapter<Any>()

    /**
     * Binds a homogeneous list of items to a layout.
     */
    val singleItem = itemBindingOf<MutableItem>(BR.item, R.layout.item)

    val pageItem = itemBindingOf<MutableItem>(BR.item, R.layout.item_page)

    /**
     * Binds multiple items types to different layouts based on class. This could have also be
     * written manually as
     * <pre>`public final OnItemBind<Object> multipleItems = new OnItemBind<Object>() {
     *
     * public void onItemBind(ItemBinding itemBinding, int position, Object item) {
     * if (String.class.equals(item.getClass())) {
     * itemBinding.set(BR.item, R.layout.item_header_footer);
     * } else if (Item.class.equals(item.getClass())) {
     * itemBinding.set(BR.item, R.layout.item);
     * }
     * }
     * };
    `</pre> *
     */
    val multipleItems = OnItemBindClass<Any>().apply {
        map<String>(BR.item, R.layout.item_header_footer)
        map<MutableItem>(BR.item, R.layout.item)
    }

    val multipleItems2 = itemBindingOf<String> { itemBinding, _, item ->
        when (item::class) {
            String::class -> itemBinding.set(BR.item, R.layout.item_header_footer)
            MutableItem::class -> itemBinding.set(BR.item, R.layout.item)
        }
    }.bindExtra(BR.item, this)

    /**
     * Define stable item ids. These are just based on position because the items happen to not
     * ever move around.
     */
    val itemIds =
        BindingListViewAdapter.ItemIds<Any> { position, _ -> position.toLong() }

    /**
     * Define page titles for a ViewPager
     */
    val pageTitles =
        BindingViewPagerAdapter.PageTitles<MutableItem> { _, item -> "Item ${item.index + 1}" }

    /**
     * Custom view holders for RecyclerView
     */
    val viewHolder =
        BindingRecyclerViewAdapter.ViewHolderFactory { binding -> MyAwesomeViewHolder(binding.root) }

    fun setCheckable(checkable: Boolean) {
        this.checkable = checkable
        for (item in items) {
            item.checkable = checkable
        }
    }

    private class MyAwesomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAddItem() {
        items.add(MutableItem(index = items.size, checkable = checkable))
    }

    override fun onRemoveItem() {
        if (items.size > 1) {
            items.removeAt(items.size - 1)
        }
    }
}
