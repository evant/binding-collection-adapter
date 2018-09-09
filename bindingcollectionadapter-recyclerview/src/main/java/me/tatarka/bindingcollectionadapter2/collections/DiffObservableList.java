package me.tatarka.bindingcollectionadapter2.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

/**
 * An {@link ObservableList} that uses {@link DiffUtil} to calculate and dispatch it's change
 * updates. Note: Consider using {@link AsyncDiffObservableList} instead as it will automatically
 * diff in a background thread.
 */
public class DiffObservableList<T> extends AbstractList<T> implements ObservableList<T> {

    private final Object LIST_LOCK = new Object();
    private List<T> list = Collections.emptyList();
    private final DiffUtil.ItemCallback<T> callback;
    private final boolean detectMoves;
    private final ListChangeRegistry listeners = new ListChangeRegistry();
    private final ObservableListUpdateCallback listCallback = new ObservableListUpdateCallback();

    /**
     * Creates a new DiffObservableList of type T.
     *
     * @param callback The callback that controls the behavior of the DiffObservableList.
     * @deprecated The same behavior can be obtained with {@link #DiffObservableList(DiffUtil.ItemCallback)},
     * though you may consider using {@link AsyncDiffObservableList} instead.
     */
    @Deprecated
    public DiffObservableList(@NonNull Callback<T> callback) {
        this(new ItemCallbackAdapter<>(callback), true);
    }

    /**
     * Creates a new DiffObservableList of type T.
     *
     * @param callback    The callback that controls the behavior of the DiffObservableList.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     * @deprecated The same behavior can be obtained with {@link #DiffObservableList(DiffUtil.ItemCallback, boolean)},
     * though you may consider using {@link AsyncDiffObservableList} instead.
     */
    @Deprecated
    public DiffObservableList(@NonNull Callback<T> callback, boolean detectMoves) {
        this(new ItemCallbackAdapter<>(callback), detectMoves);
    }

    /**
     * Creates a new DiffObservableList of type T.
     *
     * @param callback The callback that controls the behavior of the DiffObservableList.
     */
    public DiffObservableList(@NonNull DiffUtil.ItemCallback<T> callback) {
        this(callback, true);
    }

    /**
     * Creates a new DiffObservableList of type T.
     *
     * @param callback    The callback that controls the behavior of the DiffObservableList.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    public DiffObservableList(@NonNull DiffUtil.ItemCallback<T> callback, boolean detectMoves) {
        this.callback = callback;
        this.detectMoves = detectMoves;
    }

    /**
     * Calculates the list of update operations that can convert this list into the given one.
     *
     * @param newItems The items that this list will be set to.
     * @return A DiffResult that contains the information about the edit sequence to covert this
     * list into the given one.
     */
    @NonNull
    public DiffUtil.DiffResult calculateDiff(@NonNull final List<T> newItems) {
        final ArrayList<T> frozenList;
        synchronized (LIST_LOCK) {
            frozenList = new ArrayList<>(list);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    private DiffUtil.DiffResult doCalculateDiff(final List<T> oldItems, final List<T> newItems) {
        return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return newItems != null ? newItems.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                if (oldItem != null && newItem != null) {
                    return callback.areItemsTheSame(oldItem, newItem);
                }
                // If both items are null we consider them the same.
                return oldItem == null && newItem == null;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                if (oldItem != null && newItem != null) {
                    return callback.areContentsTheSame(oldItem, newItem);
                }
                return true;
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return callback.getChangePayload(oldItem, newItem);
            }
        }, detectMoves);
    }

    /**
     * Updates the contents of this list to the given one using the DiffResults to dispatch change
     * notifications.
     *
     * @param newItems   The items to set this list to.
     * @param diffResult The diff results to dispatch change notifications.
     */
    @MainThread
    public void update(@NonNull List<T> newItems, @NonNull DiffUtil.DiffResult diffResult) {
        synchronized (LIST_LOCK) {
            list = newItems;
        }
        diffResult.dispatchUpdatesTo(listCallback);
    }

    /**
     * Sets this list to the given items. This is a convenience method for calling {@link
     * #calculateDiff(List)} followed by {@link #update(List, DiffUtil.DiffResult)}.
     * <p>
     * <b>Warning!</b> If the lists are large this operation may be too slow for the main thread. In
     * that case, you should call {@link #calculateDiff(List)} on a background thread and then
     * {@link #update(List, DiffUtil.DiffResult)} on the main thread.
     *
     * @param newItems The items to set this list to.
     */
    @MainThread
    public void update(@NonNull List<T> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        diffResult.dispatchUpdatesTo(listCallback);
    }


    @Override
    public void addOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.remove(listener);
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public int size() {
        return list.size();
    }

    /**
     * A Callback class used by DiffUtil while calculating the diff between two lists.
     *
     * @deprecated Use {@link DiffUtil.ItemCallback} instead.
     */
    @Deprecated
    public interface Callback<T> {

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         * <p>
         * For example, if your items have unique ids, this method should check their id equality.
         *
         * @param oldItem The old item.
         * @param newItem The new item.
         * @return True if the two items represent the same object or false if they are different.
         */
        boolean areItemsTheSame(T oldItem, T newItem);

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * DiffUtil uses this information to detect if the contents of an item has changed.
         * <p>
         * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)} so
         * that you can change its behavior depending on your UI.
         * <p>
         * This method is called only if {@link #areItemsTheSame(T, T)} returns {@code true} for
         * these items.
         *
         * @param oldItem The old item.
         * @param newItem The new item which replaces the old item.
         * @return True if the contents of the items are the same or false if they are different.
         */
        boolean areContentsTheSame(T oldItem, T newItem);
    }

    class ObservableListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onChanged(int position, int count, Object payload) {
            listeners.notifyChanged(DiffObservableList.this, position, count);
        }

        @Override
        public void onInserted(int position, int count) {
            modCount += 1;
            listeners.notifyInserted(DiffObservableList.this, position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            modCount += 1;
            listeners.notifyRemoved(DiffObservableList.this, position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            listeners.notifyMoved(DiffObservableList.this, fromPosition, toPosition, 1);
        }
    }

    static class ItemCallbackAdapter<T> extends DiffUtil.ItemCallback<T> {
        final Callback<T> callback;

        ItemCallbackAdapter(Callback<T> callback) {
            this.callback = callback;
        }

        @Override
        public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return callback.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return callback.areContentsTheSame(oldItem, newItem);
        }
    }
}
