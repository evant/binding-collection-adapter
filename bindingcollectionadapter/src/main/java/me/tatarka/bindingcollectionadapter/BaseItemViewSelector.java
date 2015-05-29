package me.tatarka.bindingcollectionadapter;

/**
 * Created by evantatarka on 5/26/15.
 */
public abstract class BaseItemViewSelector<V extends BaseItemView, T> implements ItemViewSelector<V, T> {
    @Override
    public int count() {
        return 1;
    }
}
