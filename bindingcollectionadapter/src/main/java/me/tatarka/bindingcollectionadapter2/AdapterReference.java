package me.tatarka.bindingcollectionadapter2;

import android.databinding.ObservableList;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A weak reference for {@link BindingCollectionAdapter}.
 *
 * @param <A> the adapter type
 * @param <T> the item type
 */
final class AdapterReference<A extends BindingCollectionAdapter<T>, T> extends WeakReference<A> {
    private final ObservableList.OnListChangedCallback<? extends ObservableList<T>> callback;
    @Nullable
    private ObservableList<T> items;

    /**
     * Constructs a new instance.
     *
     * @param adapter  the adapter
     * @param queue    the reference queue
     * @param callback the callback to be registered into the adapter's item
     */
    AdapterReference(A adapter, AdapterReferenceQueue queue, ObservableList.OnListChangedCallback<? extends ObservableList<T>> callback) {
        super(adapter, queue);
        this.callback = callback;
    }

    @Override
    public A get() {
        A adapter = super.get();
        if (adapter == null) {
            unregisterCallback();
        }
        return adapter;
    }

    /**
     * Sets the adapter's items.
     *
     * @param items the items held by the adapter
     */
    void setItems(@Nullable List<T> items) {
        this.items = items instanceof ObservableList ? (ObservableList<T>) items : null;
    }

    /**
     * Unregisters the callback instance from the adapter's items.
     */
    void unregisterCallback() {
        ObservableList<T> items = this.items;
        if (items != null) {
            this.items = null;
            items.removeOnListChangedCallback(callback);
        }
    }
}
