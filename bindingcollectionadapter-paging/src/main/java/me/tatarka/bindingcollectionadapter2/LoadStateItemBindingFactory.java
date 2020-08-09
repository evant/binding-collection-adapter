package me.tatarka.bindingcollectionadapter2;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;

public interface LoadStateItemBindingFactory {

    ItemBinding<? super LoadState> create(@NonNull final PagedListCallback callback);
}
