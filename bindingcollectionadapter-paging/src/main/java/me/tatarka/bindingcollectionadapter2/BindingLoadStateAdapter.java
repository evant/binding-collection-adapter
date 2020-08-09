package me.tatarka.bindingcollectionadapter2;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BindingLoadStateAdapter extends LoadStateAdapter<RecyclerView.ViewHolder> implements BindingCollectionAdapter<LoadState> {
    private static final Object DATA_INVALIDATION = new Object();

    private ItemBinding<? super LoadState> itemBinding;
    private LayoutInflater inflater;
    @Nullable
    private BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory;
    // Currently attached recyclerview, we don't have to listen to notifications if null.
    @Nullable
    private RecyclerView recyclerView;
    @Nullable
    private LifecycleOwner lifecycleOwner;

    public BindingLoadStateAdapter(ItemBinding<? super LoadState> itemBinding) {
        this.itemBinding = itemBinding;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = null;
    }

    /**
     * Set the factory for creating view holders. If null, a default view holder will be used. This
     * is useful for holding custom state in the view holder or other more complex customization.
     */
    public void setViewHolderFactory(@Nullable BindingRecyclerViewAdapter.ViewHolderFactory factory) {
        viewHolderFactory = factory;
    }

    /**
     * Constructs a view holder for the given databinding. The default implementation is to use
     * {@link BindingRecyclerViewAdapter.ViewHolderFactory} if provided, otherwise use a default view holder.
     */
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewDataBinding binding) {
        if (viewHolderFactory != null) {
            return viewHolderFactory.createViewHolder(binding);
        } else {
            return new BindingViewHolder(binding);
        }
    }

    private static class BindingViewHolder extends RecyclerView.ViewHolder {
        BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, @NotNull LoadState loadState) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        itemBinding.onItemBind(0, loadState);
        ViewDataBinding binding = onCreateBinding(inflater, itemBinding.layoutRes(), viewGroup);
        final RecyclerView.ViewHolder holder = onCreateViewHolder(binding);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                return recyclerView != null && recyclerView.isComputingLayout();
            }

            @Override
            public void onCanceled(ViewDataBinding binding) {
                if (recyclerView == null || recyclerView.isComputingLayout()) {
                    return;
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        notifyItemChanged(position, DATA_INVALIDATION);
                    } catch (IllegalStateException e) {
                        // noop - this shouldn't be happening
                    }
                }
            }
        });
        return holder;
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (isForDataBinding(payloads)) {
            binding.executePendingBindings();
        } else {
            onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, getLoadState());
        }
    }

    private boolean isForDataBinding(List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            return false;
        }
        for (int i = 0; i < payloads.size(); i++) {
            Object obj = payloads.get(i);
            if (obj != DATA_INVALIDATION) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, @NotNull LoadState loadState) {
        // This won't be called by recyclerview since we are overriding the other overload, call
        // the other overload here in case someone is calling this directly ex: in a test.
        onBindViewHolder(viewHolder, 0, Collections.emptyList());
    }

    @Override
    public void setItemBinding(@NonNull ItemBinding<? super LoadState> itemBinding) {
        this.itemBinding = itemBinding;
    }

    @Override
    public int getStateViewType(@NotNull LoadState loadState) {
        itemBinding.onItemBind(0, loadState);
        return itemBinding.layoutRes();
    }

    @NonNull
    @Override
    public ItemBinding<? super LoadState> getItemBinding() {
        if (itemBinding == null) {
            throw new NullPointerException("itemBinding == null");
        }
        return itemBinding;
    }

    @Override
    public void setItems(@Nullable List<LoadState> items) {
        if (items != null && !items.isEmpty()) {
            setLoadState(items.get(0));
        }
    }

    @Override
    public LoadState getAdapterItem(int position) {
        return getLoadState();
    }

    @NonNull
    @Override
    public ViewDataBinding onCreateBinding(@NonNull LayoutInflater inflater, int layoutRes, @NonNull ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(@NonNull ViewDataBinding binding, int variableId, int layoutRes, int position, LoadState item) {
        tryGetLifecycleOwner();
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
        }
    }

    private void tryGetLifecycleOwner() {
        if (lifecycleOwner == null || lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            lifecycleOwner = Utils.findLifecycleOwner(recyclerView);
        }
    }
}
