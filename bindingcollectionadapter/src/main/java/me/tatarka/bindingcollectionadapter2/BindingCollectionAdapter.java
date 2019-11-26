package me.tatarka.bindingcollectionadapter2;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

/**
 * Base interface for the binding collection adapters for various components.
 *
 * @see BindingListViewAdapter
 * @see BindingViewPagerAdapter
 */
public interface BindingCollectionAdapter<T> {

    /**
     * Sets the item biding for the adapter.
     */
    void setItemBinding(@NonNull ItemBinding<? super T> itemBinding);

    /**
     * Returns the {@link ItemBinding} that the adapter that was set.
     *
     * @throws NullPointerException if the item binding was not set.
     */
    @NonNull
    ItemBinding<? super T> getItemBinding();

    /**
     * Sets the adapter's items. These items will be displayed based on the {@link ItemBinding}. If
     * you pass in an {@link ObservableList} the adapter will also update itself based on that
     * list's changes. <br/> Note that the adapter will keep a direct reference to the given list.
     * Any changes to it <em>must</em> happen on the main thread. Additionally, if you are not using
     * an {@code ObservableList}, you <em>must</em> call {@code notifyDataSetChanged()} or one of
     * the related methods.
     */
    void setItems(@Nullable List<T> items);

    /**
     * Returns the item in the adapter given position. This is useful for accessing items in the
     * adapter.
     */
    T getAdapterItem(int position);

    /**
     * Called to create a binding. An implementation should create a binding inflated with the given
     * {@code layoutId} and {@code viewGroup}. A subclass may override this and cast the result to a
     * specific layout binding to get at the generated view fields.
     */
    @NonNull
    ViewDataBinding onCreateBinding(@NonNull LayoutInflater inflater, @LayoutRes int layoutRes, @NonNull ViewGroup viewGroup);

    /**
     * Called to bind the given item to the binding. An implementation should simply set the item as
     * a variable on the binding. A subclass may override this and cast the binding to an
     * implementation specific layout binding to get at the generated view fields.
     */
    void onBindBinding(@NonNull ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item);
}
