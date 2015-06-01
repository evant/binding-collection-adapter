package me.tatarka.bindingcollectionadapter;

/**
 * Created by evan on 5/16/15.
 */
public interface ItemViewSelector<T> {
    void select(ItemView itemView, int position, T item);

    int count();
}
