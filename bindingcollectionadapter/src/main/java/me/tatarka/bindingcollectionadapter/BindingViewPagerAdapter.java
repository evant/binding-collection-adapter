package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * A {@link PagerAdapter} that binds items to layouts using the given {@link ItemView} or {@link
 * ItemViewSelector}. If you give it an {@link ObservableList} it will also updated itself based on
 * changes to that list.
 */
public class BindingViewPagerAdapter<T> extends PagerAdapter {
    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
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

    /**
     * Sets the adapter's items. These items will be displayed based on the {@link ItemView} or
     * {@link ItemViewSelector}. If you pass in an {@link ObservableList} the adapter will also
     * update itself based on that list's changes.
     */
    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(callback);
            notifyDataSetChanged();
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }

        T item = items.get(position);
        selector.select(itemView, position, item);

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemView.getLayoutRes(), container, false);
        binding.setVariable(itemView.getBindingVariable(), item);
        binding.executePendingBindings();
        container.addView(binding.getRoot());
        binding.getRoot().setTag(item);
        
        setPageTitles(new PageTitles<T>() {
            @Override
            public CharSequence getPageTitle(int position, T item) {
                return "Page Title";
            }
        });

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
        for (int i = 0 ; i < items.size(); i++) {
            if (item == items.get(i)) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends BaseOnListChangedCallback<T> {
        final WeakReference<PagerAdapter> adapterRef;

        WeakReferenceOnListChangedCallback(PagerAdapter adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            onMainThread(new OnMainThread() {
                @Override
                public void onMainThread() {
                    PagerAdapter adapter = adapterRef.get();
                    if (adapter != null) {
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
