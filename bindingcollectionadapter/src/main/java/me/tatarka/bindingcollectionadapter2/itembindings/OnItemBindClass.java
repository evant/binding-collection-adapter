package me.tatarka.bindingcollectionadapter2.itembindings;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

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
     * Returns the number of item types in the map. This is useful for {@link
     * BindingListViewAdapter#BindingListViewAdapter(int)} or {@code app:itemTypeCount} in an {@code
     * AdapterView}.
     */
    public int itemTypeCount() {
        return itemBindingMap.size();
    }

    @Override
    public void onItemBind(ItemBinding itemBinding, int position, T item) {
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
}
