package me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.listeners.PageListener;
import me.tatarka.bindingcollectionadapter.sample.*;
import me.tatarka.bindingcollectionadapter.sample.BR;

/**
 * Created by wanghaiming on 2016/9/22.
 */
public class ViewModel  {

    private PageListener mPageListener;

    private ObservableList<ItemViewModel> mItems = new ObservableArrayList<>();
    private final ItemView mItemView = ItemView.of(BR.itemViewModel,R.layout.item_with_button);

    public ObservableList<ItemViewModel> getItems() {
        return mItems;
    }

    public int getItemCount(){
        return mItems.size();
    }
    public void setItems(List<ItemViewModel> items) {

        mItems.clear();

        if((items != null)&&(items.size() > 0)){
            for(ItemViewModel item : items){
                item.setListener(mPageListener.getItemListener());
                mItems.add(item);
            }
        }
    }

    public void addItem(ItemViewModel item){
        item.setListener(mPageListener.getItemListener());
        mItems.add(item);
    }
    public void removeItem(){
        mItems.remove(mItems.size()-1);
    }
    public ItemView getItemView() {
        return mItemView;
    }

    public PageListener getPageListener() {
        return mPageListener;
    }

    public void setPageListener(PageListener pageListener) {
        mPageListener = pageListener;
    }
}
