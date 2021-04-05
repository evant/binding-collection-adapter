package me.tatarka.bindingcollectionadapter2.collections;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.LoadType;

import java.util.AbstractList;

import me.tatarka.bindingcollectionadapter2.itembindings.Load;

public class LoadStateObservableList<T> extends AbstractList<Object> implements ObservableList<Object> {

    private final ListChangeRegistry listeners = new ListChangeRegistry();
    private final ObservableList<T> backingList;

    public LoadStateObservableList(@NonNull ObservableList<T> backingList) {
        this.backingList = backingList;
        backingList.addOnListChangedCallback(new OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> sender) {
                listeners.notifyChanged(sender);
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
                if (showHeader()) {
                    positionStart++;
                }
                listeners.notifyChanged(sender, positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
                if (showHeader()) {
                    positionStart++;
                }
                listeners.notifyInserted(sender, positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
                if (showHeader()) {
                    fromPosition++;
                    toPosition++;
                }
                listeners.notifyMoved(sender, fromPosition, toPosition, itemCount);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
                if (showHeader()) {
                    positionStart++;
                }
                listeners.notifyRemoved(sender, positionStart, itemCount);
            }

            private boolean showHeader() {
                LoadState headerLoadState = getLoadState(LoadType.PREPEND);
                return displayLoadStateAsItem(headerLoadState);
            }
        });
    }

    private ArrayMap<LoadType, LoadState> loadStates = new ArrayMap<>();

    public void setLoadStates(@NonNull CombinedLoadStates combinedLoadStates) {
        setLoadState(combinedLoadStates.getPrepend(), LoadType.PREPEND);
        setLoadState(combinedLoadStates.getAppend(), LoadType.APPEND);
        setLoadState(combinedLoadStates.getRefresh(), LoadType.REFRESH);
    }

    public void setLoadState(@NonNull LoadState loadState, @NonNull LoadType loadType) {
        switch (loadType) {
            case REFRESH:
                //TODO
                break;
            case PREPEND:
                setLoadState(0, loadState, loadType);
                break;
            case APPEND:
                setLoadState(size(), loadState, loadType);
                break;
        }
    }

    private void setLoadState(int index, LoadState loadState, LoadType loadType) {
        LoadState currentLoadState = getLoadState(loadType);
        if (!currentLoadState.equals(loadState)) {
            boolean oldItem = displayLoadStateAsItem(currentLoadState);
            boolean newItem = displayLoadStateAsItem(loadState);

            if (oldItem && !newItem) {
                listeners.notifyRemoved(this, index, 1);
            } else if (newItem && !oldItem) {
                listeners.notifyInserted(this, index, 1);
            } else if (oldItem && newItem) {
                listeners.notifyChanged(this, index, 1);
            }

            loadStates.put(loadType, loadState);
        }
    }

    @NonNull
    public LoadState getLoadState(LoadType loadType) {
        LoadState currentLoadState = loadStates.get(loadType);
        if (currentLoadState == null) {
            currentLoadState = new LoadState.NotLoading(false);
        }
        return currentLoadState;
    }


    @Override
    public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<Object>> callback) {
        listeners.add(callback);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<Object>> callback) {
        listeners.remove(callback);
    }

    @Override
    public Object get(int index) {
        LoadState headerLoadState = getLoadState(LoadType.PREPEND);
        boolean showHeader = displayLoadStateAsItem(headerLoadState);
        if (showHeader && index == 0) {
            return new Load(headerLoadState, LoadType.PREPEND);
        }
        if (showHeader) {
            index--;
        }
        LoadState footerLoadState = getLoadState(LoadType.APPEND);
        boolean showFooter = displayLoadStateAsItem(footerLoadState);

        if (showFooter && index == backingList.size()) {
            return new Load(footerLoadState, LoadType.APPEND);
        }

        return backingList.get(index);
    }

    @Override
    public int size() {
        int size = backingList.size();
        for (int i = 0; i < loadStates.size(); i++) {
            if (displayLoadStateAsItem(loadStates.valueAt(i))) {
                size++;
            }
        }
        return size;
    }

    private boolean displayLoadStateAsItem(LoadState loadState) {
        return loadState instanceof LoadState.Loading || loadState instanceof LoadState.Error;
    }
}
