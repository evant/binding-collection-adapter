package me.tatarka.bindingcollectionadapter;

import android.databinding.ObservableList;
import android.support.annotation.Nullable;

import java.util.Collection;

/**
 * Base interface for the binding collection adapters for various components. Implementers of this
 * interface should also have two constructors, one that takes an {@link ItemView} and one that
 * takes an {@link ItemViewSelector}.
 *
 * @see BindingListViewAdapter
 * @see BindingRecyclerViewAdapter
 * @see BindingViewPagerAdapter
 */
public interface BindingCollectionAdapter<T> {
    /**
     * Sets the adapter's items. These items will be displayed based on the {@link ItemView} or
     * {@link ItemViewSelector}. If you pass in an {@link ObservableList} the adapter will also
     * update itself based on that list's changes.
     */
    void setItems(@Nullable Collection<T> items);

    /**
     * Returns the items bound to this adapter. If an {@link ObservableList} was given to {@link
     * #setItems(Collection)} this will return that same list. Otherwise, it will return a different
     * {@link ObservableList} that you can still modify directly to update the adapter. If {@link
     * #setItems(Collection)} was never called or was called with {@code null}, this will return
     * null.
     */
    ObservableList<T> getItems();

    /**
     * Sets a listener to listen when an binding is created or bound.
     *
     * @see BindingCollectionListener
     */
    void setBindingCollectionListener(@Nullable BindingCollectionListener<T> listener);
}
