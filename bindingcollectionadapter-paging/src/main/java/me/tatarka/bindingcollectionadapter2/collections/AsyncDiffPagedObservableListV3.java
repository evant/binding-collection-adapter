package me.tatarka.bindingcollectionadapter2.collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;
import androidx.lifecycle.Lifecycle;
import androidx.paging.AsyncPagingDataDiffer;
import androidx.paging.CombinedLoadStates;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.AbstractList;
import java.util.List;
import java.util.ListIterator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AsyncDiffPagedObservableListV3<T> extends AbstractList<T> implements ObservableList<T> {

    private final AsyncPagingDataDiffer<T> differ;
    private final ListChangeRegistry listeners = new ListChangeRegistry();

    /**
     * Creates a new DiffObservableList of type T, which runs the diff on a background thread using
     * {@link AsyncListDiffer}.
     *
     * @param callback The callback that controls the behavior of the DiffObservableList.
     */
    public AsyncDiffPagedObservableListV3(@NonNull DiffUtil.ItemCallback<T> callback) {
        this(new AsyncDifferConfig.Builder<>(callback).build());
    }

    /**
     * Creates a new AsyncDiffObservableList of type T, which runs the diff on a background thread using
     * {@link AsyncListDiffer}.
     *
     * @param config The config passed to {@code AsyncListDiffer}.
     */
    public AsyncDiffPagedObservableListV3(@NonNull AsyncDifferConfig<T> config) {
        differ = new AsyncPagingDataDiffer<>(config.getDiffCallback(), new ObservableListUpdateCallback());
    }

    /**
     * Retry the underlying paging
     */
    public void retry() {
        differ.retry();
    }

    /**
     * Refresh the underlying paging
     */
    public void refresh() {
        differ.refresh();
    }

    /**
     * Add a [CombinedLoadStates] listener to observe the loading state of the current [PagingData].
     *
     * As new [PagingData] generations are submitted and displayed, the listener will be notified to
     * reflect the current [CombinedLoadStates].
     *
     * @param listener [LoadStates] listener to receive updates.
     *
     * @see #removeLoadStateListener
     * @sample androidx.paging.samples.addLoadStateListenerSample
     */
    public void addLoadStateListener(@NonNull final Function1<CombinedLoadStates, Unit> listener) {
        differ.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
            @Override
            public Unit invoke(CombinedLoadStates combinedLoadStates) {
                listener.invoke(combinedLoadStates);
                return Unit.INSTANCE;
            }
        });
    }

    /**
     * Remove a previously registered [CombinedLoadStates] listener.
     *
     * @param listener Previously registered listener.
     * @see #addLoadStateListener
     */
    public void removeLoadStateListener(@NonNull final Function1<CombinedLoadStates, Unit> listener) {
        differ.removeLoadStateListener(listener);
    }

    /**
     * Updates the list to the given items. A diff will run in a background thread then this
     * collection will be updated.
     */
    public void update(@NonNull Lifecycle lifecycle, @Nullable PagingData<T> newItems) {
        differ.submitData(lifecycle, newItems);
    }

    @Override
    public void addOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> callback) {
        listeners.add(callback);
    }

    @Override
    public void removeOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> callback) {
        listeners.remove(callback);
    }

    @Override
    public T get(int index) {
        return differ.getItem(index);
    }

    @Override
    public int size() {
        return differ.getItemCount();
    }

    @Override
    public int indexOf(Object o) {
        return differ.snapshot().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return differ.snapshot().lastIndexOf(o);
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return differ.snapshot().subList(fromIndex, toIndex);
    }

    @Override
    public int hashCode() {
        return differ.snapshot().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof AsyncDiffPagedObservableListV3)) {
            return false;
        }
        return differ.snapshot().equals(((AsyncDiffPagedObservableListV3) o).differ.snapshot());
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return differ.snapshot().listIterator(index);
    }

    class ObservableListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onChanged(int position, int count, Object payload) {
            listeners.notifyChanged(AsyncDiffPagedObservableListV3.this, position, count);
        }

        @Override
        public void onInserted(int position, int count) {
            listeners.notifyInserted(AsyncDiffPagedObservableListV3.this, position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            listeners.notifyRemoved(AsyncDiffPagedObservableListV3.this, position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            listeners.notifyMoved(AsyncDiffPagedObservableListV3.this, fromPosition, toPosition, 1);
        }
    }
}
