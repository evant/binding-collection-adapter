package me.tatarka.bindingcollectionadapter.listeners;

import android.view.View;

/**
 * Created by wanghaiming on 2016/9/23.
 */
public class PageListener {
    private final ItemListener mItemListener;
    private final View.OnClickListener mOnClickListener;

    public PageListener(ItemListener itemListener, View.OnClickListener pageOnclickLister) {
        mItemListener = itemListener;
        mOnClickListener = pageOnclickLister;
    }

    public ItemListener getItemListener() {
        return mItemListener;
    }
    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }


}
