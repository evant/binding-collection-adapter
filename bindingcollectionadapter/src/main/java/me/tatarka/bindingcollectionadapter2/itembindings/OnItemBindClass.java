package me.tatarka.bindingcollectionadapter2.itembindings;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * An {@link OnItemBind} that selects an item view based on the class of the given item.
 * <pre>{@code
 * itemBind = new OnItemBindClass<>()
 *     .map(String.class, BR.name, R.layout.item_name)
 *     .map(Footer.class, ItemBinding.VAR_NONE, R.layout.item_footer);
 * }</pre>
 */
public class OnItemBindClass<T> implements OnItemBind<T> {

    private final LinkedHashMap<Class<? extends T>, OnItemBind<? extends T>> itemBindingMap;

    public OnItemBindClass() {
        this.itemBindingMap = new LinkedHashMap<>(2);
    }

    /**
     * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by Class.
     */
    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, final int variableId, @LayoutRes final int layoutRes) {
        itemBindingMap.put(itemClass, itemBind(variableId, layoutRes));
        return this;
    }

    /**
     * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by Class.
     */
    public <E extends T> OnItemBindClass<T> map(@NonNull Class<E> itemClass, OnItemBind<E> onItemBind) {
        itemBindingMap.put(itemClass, onItemBind);
        return this;
    }

    /**
     * Returns the number of item types in the map. This is useful for {@link
     * BindingListViewAdapter#BindingListViewAdapter(int)} or {@code app:itemTypeCount} in an {@code
     * AdapterView}.
     */
    public int itemTypeCount() {
        return itemBindingMap.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemBind(ItemBinding itemBinding, int position, T item) {
        for (Map.Entry<Class<? extends T>, OnItemBind<? extends T>> entry : itemBindingMap.entrySet()) {
            Class<? extends T> key = entry.getKey();
            if (key.isInstance(item)) {
                OnItemBind itemBind = entry.getValue();
                itemBind.onItemBind(itemBinding, position, item);
                return;
            }
        }
        throw new IllegalArgumentException("Missing class for item " + item);
    }

    @NonNull
    private OnItemBind<T> itemBind(final int variableId, @LayoutRes final int layoutRes) {
        return new OnItemBind<T>() {
            @Override
            public void onItemBind(ItemBinding itemBinding, int position, T item) {
                itemBinding.set(variableId, layoutRes);
            }
        };
    }
}
