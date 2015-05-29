package me.tatarka.bindingcollectionadapter;

import android.support.annotation.LayoutRes;

/**
 * Created by evantatarka on 5/26/15.
 */
public class PagerItemView extends BaseItemView {
    protected String pageTitle;

    public static <T> ItemViewSelector<PagerItemView, T> of(final int bindingVariable, @LayoutRes final int layoutRes) {
        return of(bindingVariable, layoutRes, null);
    }

    public static <T> ItemViewSelector<PagerItemView, T> of(final int bindingVariable, @LayoutRes final int layoutRes, final String pageTitle) {
        return new BaseItemViewSelector<PagerItemView, T>() {
            @Override
            public void select(PagerItemView itemView, int position, T item) {
                itemView.set(bindingVariable, layoutRes, pageTitle);
            }
        };
    }

    public void set(int bindingVariable, @LayoutRes int layoutRes, String pageTitle) {
        this.bindingVariable = bindingVariable;
        this.layoutRes = layoutRes;
        this.pageTitle = pageTitle;
    }

    protected String getPageTitle() {
        return pageTitle;
    }
}
