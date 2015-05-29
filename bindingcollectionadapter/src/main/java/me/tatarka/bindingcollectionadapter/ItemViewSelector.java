package me.tatarka.bindingcollectionadapter;

/**
 * Created by evan on 5/16/15.
 */
public interface ItemViewSelector<V extends BaseItemView, T> {
    void select(V itemView, int position, T item);
    int count();
}
