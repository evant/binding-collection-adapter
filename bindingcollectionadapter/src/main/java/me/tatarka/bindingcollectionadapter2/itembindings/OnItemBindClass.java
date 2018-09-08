package me.tatarka.bindingcollectionadapter2.itembindings;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

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

    private final List<Class<? extends T>> itemBindingClassList;
    private final List<OnItemBind<? extends T>> itemBindingList;

    public OnItemBindClass() {
        this.itemBindingClassList = new ArrayList<>(2);
        this.itemBindingList = new ArrayList<>(2);
    }

    /**
     * Maps the given class to the given variableId and layout. This is assignment-compatible match with the object represented by Class.
     */
    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, final int variableId, @LayoutRes final int layoutRes) {
        int index = itemBindingClassList.indexOf(itemClass);
        if (index >= 0) {
            itemBindingList.set(index, itemBind(variableId, layoutRes));
        } else {
            itemBindingClassList.add(itemClass);
            itemBindingList.add(itemBind(variableId, layoutRes));
        }
        return this;
    }

    /**
     * Maps the given class to the given {@link OnItemBind}. This is assignment-compatible match with the object represented by Class.
     */
    public <E extends T> OnItemBindClass<T> map(@NonNull Class<E> itemClass, @NonNull OnItemBind<E> onItemBind) {
        int index = itemBindingClassList.indexOf(itemClass);
        if (index >= 0) {
            itemBindingList.set(index, onItemBind);
        } else {
            itemBindingClassList.add(itemClass);
            itemBindingList.add(onItemBind);
        }
        return this;
    }

    /**
     * Returns the number of item types in the map. This is useful for {@link
     * BindingListViewAdapter#BindingListViewAdapter(int)} or {@code app:itemTypeCount} in an {@code
     * AdapterView}.
     */
    public int itemTypeCount() {
        return itemBindingClassList.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemBind(@NonNull ItemBinding itemBinding, int position, T item) {
        for (int i = 0; i < itemBindingClassList.size(); i++) {
            Class<? extends T> key = itemBindingClassList.get(i);
            if (key.isInstance(item)) {
                OnItemBind itemBind = itemBindingList.get(i);
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
            public void onItemBind(@NonNull ItemBinding itemBinding, int position, T item) {
                itemBinding.set(variableId, layoutRes);
            }
        };
    }
}
