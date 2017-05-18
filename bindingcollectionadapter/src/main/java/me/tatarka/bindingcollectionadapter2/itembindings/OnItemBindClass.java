package me.tatarka.bindingcollectionadapter2.itembindings;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;

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

    private final SimpleArrayMap<Class<? extends T>, Object> itemBindingMap;
    private final List<Class> itemBindingIndexes;

    public OnItemBindClass() {
        this.itemBindingMap = new SimpleArrayMap<>();
        this.itemBindingIndexes = new ArrayList<>(2);
    }

    /**
     * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by Class.
     */
    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, int variableId, @LayoutRes int layoutRes) {
        itemBindingMap.put(itemClass, new int[]{variableId, layoutRes});
        itemBindingIndexes.add(itemClass);
        return this;
    }

    /**
     * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by Class.
     */
    public <E extends T> OnItemBindClass<T> map(@NonNull Class<E> itemClass, OnItemBind<E> onItemBind) {
        itemBindingMap.put(itemClass, onItemBind);
        itemBindingIndexes.add(itemClass);
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

    @Override
    public void onItemBind(ItemBinding itemBinding, int position, T item) {
        for (int i = 0; i < itemBindingIndexes.size(); i++) {
            Class<? extends T> key = itemBindingIndexes.get(i);
            if (key.isInstance(item)) {
                onTypedItemBind(itemBinding, position, item, itemBindingMap.get(key));
                return;
            }
        }
        throw new IllegalArgumentException("Missing class for item " + item);
    }

    @SuppressWarnings("unchecked")
    private <E extends T> void onTypedItemBind(ItemBinding itemBinding, int position, E item, Object itemBind) {
        if (itemBind instanceof OnItemBind) {
            OnItemBind<E> values = (OnItemBind<E>) itemBind;
            values.onItemBind(itemBinding, position, item);
        } else {
            int[] values = (int[]) itemBind;
            itemBinding.reset(values[0], values[1]);
        }
    }
}
