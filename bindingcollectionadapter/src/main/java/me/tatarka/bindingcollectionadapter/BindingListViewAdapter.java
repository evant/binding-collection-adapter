package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by evan on 5/16/15.
 */
public class BindingListViewAdapter<T> extends BaseAdapter {
    public static final String ITEM_ID = "item_id";
    public static final String DROP_DOWN_LAYOUT = "drop_down_layout";
    
    @NonNull
    private final ItemView itemView;
    @NonNull
    private final ItemViewSelector<T> selector;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private ObservableList<T> items;
    private int[] layouts;
    private int[] dropDownLayouts;
    private LayoutInflater inflater;
    
    public BindingListViewAdapter(@NonNull ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    public BindingListViewAdapter(@NonNull ItemViewSelector<T> selector) {
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

    public ObservableList<T> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        selector.select(itemView, position, items.get(position));
        return itemView.getInt(ITEM_ID);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);

        ViewDataBinding binding;
        if (convertView == null) {
            int layoutRes = layouts[viewType];
            binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            binding.getRoot().setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        T item = items.get(position);
        binding.setVariable(itemView.getBindingVariable(), item);

        return binding.getRoot();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);

        ViewDataBinding binding;
        if (convertView == null) {
            int layoutRes = dropDownLayouts[viewType];
            binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);
            binding.getRoot().setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }

        T item = items.get(position);
        binding.setVariable(itemView.getBindingVariable(), item);

        return binding.getRoot();
    }

    @Override
    public int getItemViewType(int position) {
        selector.select(itemView, position, items.get(position));
        int firstEmpty = 0;
        for (int i = 0; i < layouts.length; i++) {
            if (itemView.layoutRes == layouts[i]) {
                return i;
            }
            if (layouts[i] == 0) {
                firstEmpty = i;
            }
        }
        layouts[firstEmpty] = itemView.layoutRes;
        dropDownLayouts[firstEmpty] = itemView.getInt(DROP_DOWN_LAYOUT);
        return firstEmpty;
    }

    @Override
    public int getViewTypeCount() {
        int count = selector.count();
        layouts = new int[count];
        dropDownLayouts = new int[count];
        return count;
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BaseAdapter> adapterRef;
        final Handler handler = new Handler(Looper.getMainLooper());

        WeakReferenceOnListChangedCallback(BaseAdapter adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    BaseAdapter adapter = adapterRef.get();
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
