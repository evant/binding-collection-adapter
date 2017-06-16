package me.tatarka.bindingcollectionadapter2;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * A reference queue for {@link AdapterReference}.
 */
final class AdapterReferenceQueue extends ReferenceQueue<BindingCollectionAdapter<?>> {
    /**
     * Removes {@link android.databinding.ObservableList.OnListChangedCallback} instances no longer
     * needed.
     */
    void expungeStaleCallbacks() {
        for (Reference<?> ref; (ref = poll()) != null; ) {
            if (ref instanceof AdapterReference) {
                ((AdapterReference<?, ?>) ref).unregisterCallback();
            }
        }
    }
}
