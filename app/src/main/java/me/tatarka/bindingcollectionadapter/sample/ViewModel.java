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
    public final ObservableList<String> items = new ObservableArrayList<>();

    {
        for (int i = 0; i < 3; i++) {
            items.add("Item " + (i + 1));
        }
    }

    /**
     * ItemView of a single type
     */
    public final ItemView singleItemView = ItemView.of(BR.item, R.layout.item);

    /**
     * ItemView of multiple types based on the data.
     */
    public final ItemViewSelector<String> multipleItemViews = new ItemViewSelector<String>() {
        @Override
        public void select(ItemView itemView, int position, String item) {
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
    public final BindingListViewAdapter.ItemIds<String> itemIds = new BindingListViewAdapter.ItemIds<String>() {
        @Override
        public long getItemId(int position, String item) {
            return position;
        }
    };

    /**
     * Define page titles for a ViewPager
     */
    public final BindingViewPagerAdapter.PageTitles<String> pageTitles = new BindingViewPagerAdapter.PageTitles<String>() {
        @Override
        public CharSequence getPageTitle(int position, String item) {
            return item;
        }
    };

    public void addItem() {
        items.add("Item " + (items.size() + 1));
    }

    public void removeItem() {
        if (items.size() > 1) {
            items.remove(items.size() - 1);
        }
    }
}
