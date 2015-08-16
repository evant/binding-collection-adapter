package me.tatarka.bindingcollectionadapter;

/**
 * Unify {@link ItemView} and {@link ItemViewSelector} to simplify BindingAdapters. This is a
 * poor-man's union type.
 */
public class ItemViewArg<T> {
    public static <T> ItemViewArg<T> of(ItemView itemView) {
        return new ItemViewArg<>(itemView);
    }

    public static <T> ItemViewArg<T> of(ItemViewSelector<T> selector) {
        return new ItemViewArg<>(selector);
    }

    final ItemView itemView;
    final ItemViewSelector<T> selector;

    private ItemViewArg(ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    private ItemViewArg(ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }
}
