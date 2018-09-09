package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.NonNull;

/**
 * Callback for setting up a {@link ItemBinding} for an item in the collection.
 *
 * @param <T> the item type
 */
public interface OnItemBind<T> {
    /**
     * Called on each item in the collection, allowing you to modify the given {@link ItemBinding}.
     * Note that you should not do complex processing in this method as it's called many times.
     */
    void onItemBind(@NonNull ItemBinding itemBinding, int position, T item);
}
