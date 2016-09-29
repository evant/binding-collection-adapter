package me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import me.tatarka.bindingcollectionadapter.listeners.ItemListener;

/**
 * Created by wanghaiming on 2016/9/22.
 */
public class ItemViewModel {


    private String mBookName;
    private final ObservableInt mBookCount = new ObservableInt(0);
    private boolean mCheckable;
    private final ObservableBoolean mChecked = new ObservableBoolean(false);

    private ItemListener mListener;

    public ItemViewModel(String bookName, int bookCount,boolean checkable, boolean checked) {
        mBookName = bookName;
        this.mCheckable = checkable;
        this.mChecked.set(checked);
        mBookCount.set(bookCount);
    }

    public boolean isCheckable() {
        return mCheckable;
    }

    public void setCheckable(boolean checkable) {
        this.mCheckable = checkable;
    }

    public ObservableBoolean getChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked.set(checked);
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String bookName) {
        mBookName = bookName;
    }

    public ObservableInt getBookCount() {
        return mBookCount;
    }
    public void setBookCount(int count){
        mBookCount.set(count);
    }

    public ItemListener getListener() {
        return mListener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }
}
