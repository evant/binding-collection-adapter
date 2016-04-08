package me.tatarka.bindingcollectionadapter.itemviews;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * An {@link ItemViewSelector} that selects an item view based on the class of the given item.
 * <pre>{@code
 * selector = ItemViewClassSelector.builder()
 *     .put(String.class, BR.name, R.layout.item_name)
 *     .put(Footer.class, ItemView.BINDING_VARIABLE_NONE, R.layout.item_footer)
 *     .build();
 * }</pre>
 */
public class ItemViewClassSelector<T> implements ItemViewSelector<T> {

    /**
     * Returns a new builder to construct an {@code ItemViewClassSelector} instance.
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    private final SimpleArrayMap<Class<? extends T>, ItemView> itemViewMap;

    ItemViewClassSelector(SimpleArrayMap<Class<? extends T>, ItemView> itemViewMap) {
        this.itemViewMap = itemViewMap;
    }

    @Override
    public void select(ItemView itemView, int position, T item) {
        ItemView itemItemView = itemViewMap.get(item.getClass());
        if (itemItemView != null) {
            itemView.set(itemItemView.bindingVariable(), itemItemView.layoutRes());
        } else {
            throw new IllegalArgumentException("Missing class for item " + item);
        }
    }

    @Override
    public int viewTypeCount() {
        return itemViewMap.size();
    }

    public static class Builder<T> {
        private final SimpleArrayMap<Class<? extends T>, ItemView> itemViewMap = new SimpleArrayMap<>();

        Builder() {
        }

        public Builder<T> put(@NonNull Class<? extends T> itemClass, int bindingVariable, @LayoutRes int layoutRes) {
            itemViewMap.put(itemClass, ItemView.of(bindingVariable, layoutRes));
            return this;
        }

        public Builder<T> put(@NonNull Class<? extends T> itemClass, @NonNull ItemView itemView) {
            itemViewMap.put(itemClass, itemView);
            return this;
        }

        public ItemViewClassSelector<T> build() {
            return new ItemViewClassSelector<>(itemViewMap);
        }
    }
}
