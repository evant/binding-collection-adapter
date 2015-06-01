package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by evantatarka on 5/26/15.
 */
public class BindingViewPagerAdapter<T> extends PagerAdapter {
    private final WeakReferenceOnListChangedListener callback = new WeakReferenceOnListChangedListener(this);
    private final PagerItemView itemView = new PagerItemView();
    private final ItemViewSelector<PagerItemView, T> selector;
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private SparseArrayCompat<String> titles = new SparseArrayCompat<>();

    public BindingViewPagerAdapter(ItemViewSelector<PagerItemView, T> selector, @Nullable Collection<T> items) {
        if (selector == null) {
            throw new IllegalArgumentException("ListItemViewSelector must not be null");
        }
        this.selector = selector;
        setItems(items);
    }

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

    @Override
    public int getCount() {
        return selector.count();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }

        T item = items.get(position);
        selector.select(itemView, position, item);
        titles.put(position, itemView.getPageTitle());

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemView.getLayoutRes(), container, false);
        binding.setVariable(itemView.getBindingVariable(), item);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeAllViews();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private static class WeakReferenceOnListChangedListener extends ObservableList.OnListChangedCallback {
        final WeakReference<PagerAdapter> adapterRef;
        final Handler handler = new Handler(Looper.getMainLooper());

        WeakReferenceOnListChangedListener(PagerAdapter adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            handler.post(new Runnable() {
                @Override
                public void run() {
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
}
