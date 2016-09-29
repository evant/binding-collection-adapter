package me.tatarka.bindingcollectionadapter.listeners;

import android.view.View;

/**
 * Created by wanghaiming on 2016/9/23.
 */
public class ItemListener {

    private final View.OnClickListener mOnClickListener;
    private final View.OnLongClickListener mOnLongClickListener;

    public ItemListener(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        mOnClickListener = onClickListener;
        mOnLongClickListener = onLongClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }


    public View.OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }


}
