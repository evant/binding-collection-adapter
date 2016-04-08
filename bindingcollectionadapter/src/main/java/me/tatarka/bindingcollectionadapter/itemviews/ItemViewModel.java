package me.tatarka.bindingcollectionadapter.itemviews;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Implement this interface on yor items to use with {@link ItemViewModelSelector}.
 */
public interface ItemViewModel {
    /**
     * Set the binding variable and layout of the given view.
     * <pre>{@code
     * itemView.set(BR.item, R.layout.item);
     * }</pre>
     */
    void itemView(ItemView itemView);
}
