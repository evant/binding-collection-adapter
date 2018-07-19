package me.tatarka.bindingcollectionadapter.sample;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList;
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass;

public class MyViewModel extends ViewModel {
    private boolean checkable;

    public final ObservableList<ItemViewModel> items = new ObservableArrayList<>();

    /**
     * Items merged with a header on top and footer on bottom.
     */
    public final MergeObservableList<Object> headerFooterItems = new MergeObservableList<>()
            .insertItem("Header")
            .insertList(items)
            .insertItem("Footer");

    /**
     * Custom adapter that logs calls.
     */
    public final LoggingRecyclerViewAdapter<Object> adapter = new LoggingRecyclerViewAdapter<>();

    public MyViewModel() {
        for (int i = 0; i < 3; i++) {
            items.add(new ItemViewModel(i, checkable));
        }
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    /**
     * Binds a homogeneous list of items to a layout.
     */
    public final ItemBinding<ItemViewModel> singleItem = ItemBinding.of(BR.item, R.layout.item);

    /**
     * Binds multiple items types to different layouts based on class. This could have also be
     * written manually as
     * <pre>{@code
     * public final OnItemBind<Object> multipleItems = new OnItemBind<Object>() {
     *     @Override
     *     public void onItemBind(ItemBinding itemBinding, int position, Object item) {
     *         if (String.class.equals(item.getClass())) {
     *             itemBinding.set(BR.item, R.layout.item_header_footer);
     *         } else if (ItemViewModel.class.equals(item.getClass())) {
     *             itemBinding.set(BR.item, R.layout.item);
     *         }
     *     }
     * };
     * }</pre>
     */
    public final OnItemBindClass<Object> multipleItems = new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.item_header_footer)
            .map(ItemViewModel.class, BR.item, R.layout.item);

    /**
     * Define stable item ids. These are just based on position because the items happen to not
     * every move around.
     */
    public final BindingListViewAdapter.ItemIds<Object> itemIds = new BindingListViewAdapter.ItemIds<Object>() {
        @Override
        public long getItemId(int position, Object item) {
            return position;
        }
    };

    /**
     * Define page titles for a ViewPager
     */
    public final BindingViewPagerAdapter.PageTitles<ItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<ItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, ItemViewModel item) {
            return "Item " + (item.getIndex() + 1);
        }
    };

    /**
     * Custom view holders for RecyclerView
     */
    public final BindingRecyclerViewAdapter.ViewHolderFactory viewHolder = new BindingRecyclerViewAdapter.ViewHolderFactory() {
        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
            return new MyAwesomeViewHolder(binding.getRoot());
        }
    };

    private static class MyAwesomeViewHolder extends RecyclerView.ViewHolder {
        public MyAwesomeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addItem() {
        items.add(new ItemViewModel(items.size(), checkable));
    }

    public void removeItem() {
        if (items.size() > 1) {
            items.remove(items.size() - 1);
        }
    }
}
