package me.tatarka.bindingcollectionadapter;

import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
     *
     * @deprecated This method had 2 uses which could either be done an alternative way or were
     * wrong. <ol> <li>If you want to access the backing collection, just use that collection
     * directly. It should be your responsibility to hold a reference to it and determine if it's
     * observable or not.</li> <li>If you want to get an item at a certain position for a custom
     * adapter implementation, this won't always be correct. The adapter keeps a separate copy of
     * the collection that is only modified on the main thread. If you add items to the backing
     * collection on a separate thread they may get out of sync. Instead, you should use {@link
     * #getAdapterItem(int)} instead.</li></ol>
     */
    @Deprecated
    ObservableList<T> getItems();

    /**
     * Returns the item in the adapter given position. If you need access to this in a custom
     * adapter subclass you should use this instead of accessing the collection directly because the
     * adapter keeps a second copy of the list that is only updated on the main thread.
     */
    T getAdapterItem(int position);

    /**
     * Called to create a binding. An implementation should create a binding inflated with the given
     * {@code layoutId} and {@code viewGroup}. A subclass may override this and cast the result to a
     * specific layout binding to get at the generated view fields.
     */
    ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup);

    /**
     * Called to bind the given item to the binding. An implementation should simply set the item as
     * a variable on the binding. A subclass may override this and cast the binding to an
     * implementation specific layout binding to get at the generated view fields.
     */
    void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item);
}
