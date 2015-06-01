package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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
    public static final String TITLE = "title";

    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private SparseArrayCompat<CharSequence> titles = new SparseArrayCompat<>();

    public BindingViewPagerAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    public BindingViewPagerAdapter(@NonNull ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
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
        titles.put(position, (CharSequence) itemView.get(TITLE));

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

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<PagerAdapter> adapterRef;
        final Handler handler = new Handler(Looper.getMainLooper());

        WeakReferenceOnListChangedCallback(PagerAdapter adapter) {
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
