package me.tatarka.bindingcollectionadapter2.collections;

import androidx.annotation.NonNull;
import androidx.paging.CombinedLoadStates;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public abstract class LoadStateListener implements Function1<CombinedLoadStates, Unit> {
    protected abstract void onLoadStateChanged(@NonNull CombinedLoadStates loadStates);

    @Override
    public final Unit invoke(CombinedLoadStates loadStates) {
        onLoadStateChanged(loadStates);
        return Unit.INSTANCE;
    }
}
