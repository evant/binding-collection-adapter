package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
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
import java.util.Collection;
import java.util.List;

/**
 * A {@link PagerAdapter} that binds items to layouts using the given {@link ItemView} or {@link
 * ItemViewSelector}. If you give it an {@link ObservableList} it will also updated itself based on
 * changes to that list.
 */
public class BindingViewPagerAdapter<T> extends PagerAdapter implements BindingCollectionAdapter<T> {
    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    // This is what the viewpager sees. It will only be modified on the main thread.
    private final List<T> boundItems = new ArrayList<>();
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private PageTitles<T> pageTitles;

    /**
     * Constructs a new instance with the given {@link ItemView}.
     */
    public BindingViewPagerAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    /**
     * Constructs a new instance with the given {@link ItemViewSelector}.
     */
    public BindingViewPagerAdapter(@NonNull ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }

    @Override
    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(callback);
            this.boundItems.clear();
            notifyDataSetChanged();
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            this.boundItems.addAll(items);
            notifyDataSetChanged();
            this.items.addOnListChangedCallback(callback);
        } else if (items != null) {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(callback);
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }

    @Override
    public ObservableList<T> getItems() {
        return items;
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {
        boolean result = binding.setVariable(bindingVariable, item);
        if (!result) {
            BindingCollectionAdapters.throwMissingVariable(binding, bindingVariable, layoutRes);
        }
        binding.executePendingBindings();
    }

    /**
     * Sets the page titles for the adapter.
     */
    public void setPageTitles(@Nullable PageTitles<T> pageTitles) {
        this.pageTitles = pageTitles;
    }

    @Override
    public int getCount() {
        return boundItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles == null ? null : pageTitles.getPageTitle(position, boundItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }

        T item = boundItems.get(position);
        selector.select(itemView, position, item);

        ViewDataBinding binding = onCreateBinding(inflater, itemView.getLayoutRes(), container);

        if (itemView.getBindingVariable() != ItemView.BINDING_VARIABLE_NONE) {
            onBindBinding(binding, itemView.getBindingVariable(), itemView.getLayoutRes(), position, item);
        }

        container.addView(binding.getRoot());
        binding.getRoot().setTag(item);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        T item = (T) ((View) object).getTag();
        for (int i = 0; i < boundItems.size(); i++) {
            if (item == boundItems.get(i)) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends BaseOnListChangedCallback<T> {
        final WeakReference<BindingViewPagerAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingViewPagerAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingViewPagerAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            final List<T> changedItems = new ArrayList<>(adapter.items);
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    BindingViewPagerAdapter<T> adapter = adapterRef.get();
                    if (adapter != null) {
                        adapter.boundItems.clear();
                        adapter.boundItems.addAll(changedItems);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
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
