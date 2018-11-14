package me.tatarka.bindingcollectionadapter2;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PagerAdapter} that binds items to layouts using the given {@link ItemBinding} or {@link
 * OnItemBind}. If you give it an {@link ObservableList} it will also updated itself based on
 * changes to that list.
 */
public class BindingViewPagerAdapter<T> extends PagerAdapter implements BindingCollectionAdapter<T> {
    private ItemBinding<T> itemBinding;
    private WeakReferenceOnListChangedCallback<T> callback;
    private List<T> items;
    private LayoutInflater inflater;
    private PageTitles<T> pageTitles;
    @Nullable
    private LifecycleOwner lifecycleOwner;
    private boolean failedToGetLifecycleOwner;
    private List<View> views = new ArrayList<>();

    @Override
    public void setItemBinding(ItemBinding<T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    /**
     * Sets the lifecycle owner of this adapter to work with {@code LiveData}.
     * This is normally not necessary, but due to an androidx limitation, you need to set this if
     * the containing view is <em>not</em> using databinding.
     */
    public void setLifecycleOwner(@Nullable LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        for (View view : views) {
            ViewDataBinding binding = DataBindingUtil.getBinding(view);
            if (binding != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
        }
    }

    @Override
    public ItemBinding<T> getItemBinding() {
        return itemBinding;
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (this.items instanceof ObservableList) {
            ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
            callback = null;
        }
        if (items instanceof ObservableList) {
            callback = new WeakReferenceOnListChangedCallback<T>(this, (ObservableList<T>) items);
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
            binding.setLifecycleOwner(lifecycleOwner);
        }
    }

    /**
     * Sets the page titles for the adapter.
     */
    public void setPageTitles(@Nullable PageTitles<T> pageTitles) {
        this.pageTitles = pageTitles;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles == null ? null : pageTitles.getPageTitle(position, items.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }

        tryGetLifecycleOwner(container);

        T item = items.get(position);
        itemBinding.onItemBind(position, item);

        ViewDataBinding binding = onCreateBinding(inflater, itemBinding.layoutRes(), container);
        View view = binding.getRoot();

        onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, item);

        container.addView(view);
        view.setTag(item);
        views.add(view);
        return view;
    }

    private void tryGetLifecycleOwner(ViewGroup parent) {
        if (!failedToGetLifecycleOwner && lifecycleOwner == null) {
            lifecycleOwner = Utils.findLifecycleOwner(parent);
            if (lifecycleOwner == null) {
                failedToGetLifecycleOwner = true;
            }
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        views.remove(view);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemPosition(@NonNull Object object) {
        T item = (T) ((View) object).getTag();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (item == items.get(i)) {
                    return i;
                }
            }
        }
        return POSITION_NONE;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingViewPagerAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingViewPagerAdapter<T> adapter, ObservableList<T> items) {
            this.adapterRef = AdapterReferenceCollector.createRef(adapter, items, this);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingViewPagerAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }
    }

    public interface PageTitles<T> {
        CharSequence getPageTitle(int position, T item);
    }
}
