package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;

/**
 * Created by evantatarka on 5/26/15.
 */
public class ListItemView extends BaseItemView {
    @LayoutRes
    protected int dropDownLayoutRes;

    public static <T> ItemViewSelector<ListItemView, T> of(final int bindingVariable, @LayoutRes final int layoutRes) {
        return of(bindingVariable, layoutRes, 0);
    }

    public static <T> ItemViewSelector<ListItemView, T> of(final int bindingVariable, @LayoutRes final int layoutRes, @LayoutRes final int dropDownLayoutRes) {
       return new BaseItemViewSelector<ListItemView, T>() {
           @Override
           public void select(ListItemView itemView, int position, T item) {
               itemView.set(bindingVariable, layoutRes, dropDownLayoutRes);
           }
       };
    }

    public void set(int bindingVariable, @LayoutRes int layoutRes, @LayoutRes int dropDownLayoutRes) {
        this.bindingVariable = bindingVariable;
        this.layoutRes = layoutRes;
        this.dropDownLayoutRes = dropDownLayoutRes;
    }

    protected int getDropDownLayoutRes() {
        return dropDownLayoutRes;
    }
}
