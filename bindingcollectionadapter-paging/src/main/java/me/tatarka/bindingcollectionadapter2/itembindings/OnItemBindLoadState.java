package me.tatarka.bindingcollectionadapter2.itembindings;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import me.tatarka.bindingcollectionadapter2.PagedDataCallback;

public class OnItemBindLoadState<T> implements OnItemBind<Object> {

    private final OnItemBind<T> itemBind;
    private final OnItemBind<Load> combinedLoadStatesItemBind;
    private final int pagingCallbackVariableId;
    private PagedDataCallback pagedDataCallback;

    OnItemBindLoadState(OnItemBind<T> itemBind, OnItemBind<Load> loadStateOnItemBind, int pagingCallbackVariableId) {
        this.itemBind = itemBind;
        this.combinedLoadStatesItemBind = loadStateOnItemBind;
        this.pagingCallbackVariableId = pagingCallbackVariableId;
    }

    public void setPagedDataCallback(PagedDataCallback pagedDataCallback) {
        this.pagedDataCallback = pagedDataCallback;
    }

    public static class Builder<T> {
        private OnItemBind<T> itemBind;
        private OnItemBind<Load> loadState;
        private OnItemBind<androidx.paging.LoadState> headerLoadState;
        private OnItemBind<androidx.paging.LoadState> footerLoadState;
        private int pagingCallbackVariableId;

        public OnItemBindLoadState<T> build() {
            if (itemBind == null) {
                throw new IllegalStateException("itemBind must not be null");
            }
            if (loadState == null && headerLoadState == null && footerLoadState == null) {
                throw new IllegalStateException("loadState itemBind must not be null");
            }
            if (loadState == null) {
                loadState = new OnItemBind<Load>() {
                    @Override
                    public void onItemBind(@NonNull ItemBinding itemBinding, int position, Load item) {
                        switch (item.getLoadType()) {
                            case REFRESH:
                                //TODO
                                break;
                            case PREPEND:
                                headerLoadState.onItemBind(itemBinding, position, item.getLoadState());
                                break;
                            case APPEND:
                                footerLoadState.onItemBind(itemBinding, position, item.getLoadState());
                                break;
                        }
                    }
                };
            }
            return new OnItemBindLoadState<>(itemBind, loadState, pagingCallbackVariableId);
        }

        public Builder<T> item(int variableId, @LayoutRes int layoutRes) {
            this.itemBind = itemBind(variableId, layoutRes);
            return this;
        }

        public Builder<T> item(@NonNull OnItemBind<T> onItemBind) {
            this.itemBind = onItemBind;
            return this;
        }

        public Builder<T> headerLoadState(@NonNull OnItemBind<androidx.paging.LoadState> onItemBind) {
            this.headerLoadState = onItemBind;
            return this;
        }

        public Builder<T> headerLoadState(int variableId, @LayoutRes int layoutRes) {
            this.headerLoadState = itemBind(variableId, layoutRes);
            return this;
        }

        public Builder<T> footerLoadState(@NonNull OnItemBind<androidx.paging.LoadState> onItemBind) {
            this.footerLoadState = onItemBind;
            return this;
        }

        public Builder<T> footerLoadState(int variableId, @LayoutRes int layoutRes) {
            this.footerLoadState = itemBind(variableId, layoutRes);
            return this;
        }

        public Builder<T> pagingCallbackVariableId(int pagingCallbackVariableId) {
            this.pagingCallbackVariableId = pagingCallbackVariableId;
            return this;
        }

        @NonNull
        private static <T> OnItemBind<T> itemBind(final int variableId, @LayoutRes final int layoutRes) {
            return new OnItemBind<T>() {
                @Override
                public void onItemBind(@NonNull ItemBinding itemBinding, int position, T item) {
                    itemBinding.set(variableId, layoutRes);
                }
            };
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemBind(@NonNull ItemBinding itemBinding, int position, Object item) {
        if (item instanceof Load) {
            itemBinding.bindExtra(pagingCallbackVariableId, pagedDataCallback);
            combinedLoadStatesItemBind.onItemBind(itemBinding, position, (Load) item);
        } else {
            itemBind.onItemBind(itemBinding, position, (T) item);
        }
    }
}
