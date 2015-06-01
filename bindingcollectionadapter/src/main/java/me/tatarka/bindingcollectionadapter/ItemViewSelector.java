package me.tatarka.bindingcollectionadapter;

/**
 * Modify an {@link ItemView} based on the given position and item. This allows you to have
 * different layouts in the same collection. You may want to subclass {@link BaseItemViewSelector}
 * instead if you don't need to use {@link #viewTypeCount()}.
 */
public interface ItemViewSelector<T> {
    /**
     * Called on each item in the collection, allowing you to modify the given {@link ItemView}.
     * Note that you should not do complex processing in this method as it's called many times.
     */
    void select(ItemView itemView, int position, T item);

    /**
     * Return the number of <em>different</em> layouts that you will be displaying. Currently this
     * is only used in {@link BindingListViewAdapter}.
     */
    int viewTypeCount();
}
