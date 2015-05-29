package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;
/**
 * Created by evantatarka on 5/26/15.
 */
public abstract class BaseItemView {
    protected int bindingVariable;
    @LayoutRes
    protected int layoutRes;

    public void set(int bindingVariable, @LayoutRes int layoutRes) {
        this.bindingVariable = bindingVariable;
        this.layoutRes = layoutRes;
    }

    protected int getBindingVariable() {
        return bindingVariable;
    }

    @LayoutRes
    protected int getLayoutRes() {
        return layoutRes;
    }
}
