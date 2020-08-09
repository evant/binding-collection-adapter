package me.tatarka.bindingcollectionadapter2.itembindings;

import androidx.annotation.Nullable;
import androidx.paging.LoadState;
import androidx.paging.LoadType;

public final class Load {
    private final LoadState loadState;
    private final LoadType loadType;

    public Load(LoadState loadState, LoadType loadType) {
        this.loadState = loadState;
        this.loadType = loadType;
    }

    public LoadState getLoadState() {
        return loadState;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public boolean isLoading() {
        return loadState instanceof LoadState.Loading;
    }

    public boolean isError() {
        return loadState instanceof LoadState.Error;
    }

    @Nullable
    public Throwable getError() {
        if (isError()) {
            return ((LoadState.Error) loadState).getError();
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Load loadState1 = (Load) o;
        if (!loadState.equals(loadState1.loadState)) return false;
        return loadType == loadState1.loadType;
    }

    @Override
    public int hashCode() {
        int result = loadState.hashCode();
        result = 31 * result + loadType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Load{" +
                "loadState=" + loadState +
                ", loadType=" + loadType +
                '}';
    }
}
