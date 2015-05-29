package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;

/**
 * Created by evantatarka on 5/26/15.
 */
public class RecyclerItemView extends BaseItemView {
    public static <T> ItemViewSelector<RecyclerItemView, T> of(final int bindingVariable, final int layoutRes) {
        return new BaseItemViewSelector<RecyclerItemView, T>() {
            @Override
            public void select(RecyclerItemView itemView, int position, T item) {
                itemView.set(bindingVariable, layoutRes);
            }
        };
    }

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
