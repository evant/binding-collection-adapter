package me.tatarka.bindingcollectionadapter2.itembindings;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;

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

    private final SimpleArrayMap<Class<? extends T>, int[]> itemBindingMap;
    private SimpleArrayMap<Class<? extends T>, SparseArray<Object>> itemExtraBindingMap;

    public OnItemBindClass() {
        this.itemBindingMap = new SimpleArrayMap<>();
    }

    /**
     * Maps the given class to the given variableId and layout. This is an exact match, no
     * inheritance it taken into account.
     */
    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, int variableId, @LayoutRes int layoutRes) {
        itemBindingMap.put(itemClass, new int[]{variableId, layoutRes});
        return this;
    }

    /**
     * Maps the given class to the given variableId and extra variable. This is an exact match, no
     * inheritance it taken into account.
     */
    public OnItemBindClass<T> mapExtra(@NonNull Class<? extends T> itemClass, int variableId, Object value) {
        SparseArray<Object> extra = null;
        if (itemExtraBindingMap == null) {
            itemExtraBindingMap = new SimpleArrayMap<>();
        } else {
            extra = itemExtraBindingMap.get(itemClass);
        }
        if (extra == null) {
            extra = new SparseArray<>(1);
        }
        extra.put(variableId, value);

        itemExtraBindingMap.put(itemClass, extra);
        return this;
    }

    /**
     * Maps the given class to the given variableId and extra variable. This is an exact match, no
     * inheritance it taken into account.
     */
    public <E extends T> OnItemBindClass<T> mapExtra(@NonNull Class<E> itemClass, int variableId, PropertyResolver<E> value) {
        return mapExtra(itemClass, variableId, (Object) value);
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
        itemBinding.clearExtra();
        if (itemExtraBindingMap != null && !itemExtraBindingMap.isEmpty()) {
            for (int i = 0; i < itemExtraBindingMap.size(); i++) {
                Class<? extends T> key = itemExtraBindingMap.keyAt(i);
                if (key.isInstance(item)) {
                    SparseArray<Object> extraBindings = itemExtraBindingMap.valueAt(i);
                    if (extraBindings != null) {
                        for (int j = 0, size = extraBindings.size(); j < size; j++) {
                            int variableId = extraBindings.keyAt(j);
                            Object value = extraBindings.valueAt(j);
                            if (value instanceof PropertyResolver) {
                                value = ((PropertyResolver<T>) value).resolve(item);
                            }
                            itemBinding.bindExtra(variableId, value);
                        }
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < itemBindingMap.size(); i++) {
            Class<? extends T> key = itemBindingMap.keyAt(i);
            if (key.isInstance(item)) {
                int[] values = itemBindingMap.valueAt(i);
                itemBinding.set(values[0], values[1]);
                return;
            }
        }
        throw new IllegalArgumentException("Missing class for item " + item);
    }

    public interface PropertyResolver<T> {
        Object resolve(T t);
    }
}
