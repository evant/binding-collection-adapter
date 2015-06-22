package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import me.tatarka.bindingcollectionadapter.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by evan on 5/31/15.
 */
public class ViewModel {
    public final ObservableList<ItemViewModel> items = new ObservableArrayList<>();

    {
        for (int i = 0; i < 3; i++) {
            items.add(new ItemViewModel(i));
        }
    }

    /**
     * ItemView of a single type
     */
    public final ItemView singleItemView = ItemView.of(BR.item, R.layout.item);

    /**
     * ItemView of multiple types based on the data.
     */
    public final ItemViewSelector<ItemViewModel> multipleItemViews = new ItemViewSelector<ItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ItemViewModel item) {
            itemView.setBindingVariable(BR.item)
                    .setLayoutRes(position == 0 ? R.layout.item_header : R.layout.item);
        }

        // Only needed if you are using in a ListView
        @Override
        public int viewTypeCount() {
            return 2;
        }
    };

    /**
     * Define stable item ids
     */
    public final BindingListViewAdapter.ItemIds<ItemViewModel> itemIds = new BindingListViewAdapter.ItemIds<ItemViewModel>() {
        @Override
        public long getItemId(int position, ItemViewModel item) {
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

    public void addItem() {
        items.add(new ItemViewModel(items.size()));
    }

    public void removeItem() {
        if (items.size() > 1) {
            items.remove(items.size() - 1);
        }
    }
}
