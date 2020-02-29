package me.tatarka.bindingcollectionadapter2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;

/**
 * A {@link PagerAdapter} that binds items to layouts using the given {@link ItemBinding} or {@link
 * OnItemBind}. If you give it an {@link ObservableList} it will also updated itself based on
 * changes to that list.
 */
public class BindingViewPagerAdapter<T> extends PagerAdapter implements BindingCollectionAdapter<T> {
    private ItemBinding<? super T> itemBinding;
    private WeakReferenceOnListChangedCallback<T> callback;
    private List<T> items;
    private LayoutInflater inflater;
    @Nullable
    private PageTitles<T> pageTitles;
    @Nullable
    private LifecycleOwner lifecycleOwner;
    private List<View> views = new ArrayList<>();

    public BindingViewPagerAdapter() {
    }

    /**
     * Constructs a new instance with the given item binding.
     */
    public BindingViewPagerAdapter(@NonNull ItemBinding<? super T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    @Override
    public void setItemBinding(@NonNull ItemBinding<? super T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    /**
     * Sets the lifecycle owner of this adapter to work with {@link androidx.lifecycle.LiveData}.
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

    @NonNull
    @Override
    public ItemBinding<? super T> getItemBinding() {
        if (itemBinding == null) {
            throw new NullPointerException("itemBinding == null");
        }
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

    @NonNull
    @Override
    public ViewDataBinding onCreateBinding(@NonNull LayoutInflater inflater, @LayoutRes int layoutRes, @NonNull ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(@NonNull ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }
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

    @Nullable
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

    private void tryGetLifecycleOwner(View view) {
        if (lifecycleOwner == null || lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            lifecycleOwner = Utils.findLifecycleOwner(view);
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
        @Nullable
        CharSequence getPageTitle(int position, T item);
    }
}
