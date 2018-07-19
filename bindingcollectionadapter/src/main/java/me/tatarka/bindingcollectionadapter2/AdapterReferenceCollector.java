package me.tatarka.bindingcollectionadapter2;

import androidx.databinding.ObservableList;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class AdapterReferenceCollector {

    static final ReferenceQueue<Object> QUEUE = new ReferenceQueue<>();
    static PollReferenceThread thread;

    /**
     * Creates a {@link WeakReference} that will unregister the given callback from the given observable list when the adapter gets collected.
     */
    static <T, A extends BindingCollectionAdapter<T>> WeakReference<A> createRef(A adapter, ObservableList<T> items, ObservableList.OnListChangedCallback callback) {
        if (thread == null || !thread.isAlive()) {
            thread = new PollReferenceThread();
            thread.start();
        }
        return new AdapterRef<>(adapter, items, callback);
    }


    private static class PollReferenceThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Reference<?> ref = QUEUE.remove();
                    if (ref instanceof AdapterRef) {
                        ((AdapterRef) ref).unregister();
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    static class AdapterRef<T, A extends BindingCollectionAdapter<T>> extends WeakReference<A> {
        private final ObservableList<T> items;
        private final ObservableList.OnListChangedCallback callback;

        AdapterRef(A referent, ObservableList<T> items, ObservableList.OnListChangedCallback callback) {
            super(referent, QUEUE);
            this.items = items;
            this.callback = callback;
        }

        void unregister() {
            items.removeOnListChangedCallback(callback);
        }
    }
}
