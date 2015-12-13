package me.tatarka.bindingcollectionadapter;

/**
 * Unify {@link ItemView} and {@link ItemViewSelector} to simplify BindingAdapters.
 */
public class ItemViewArg<T> {
    public static <T> ItemViewArg<T> of(ItemView itemView) {
        return new ItemViewArg<>(itemView);
    }

    public static <T> ItemViewArg<T> of(ItemViewSelector<T> selector) {
        return new ItemViewArg<>(selector);
    }

    private final ItemView itemView;
    private final ItemViewSelector<T> selector;

    private ItemViewArg(ItemView itemView) {
        this.itemView = itemView;
        this.selector = BaseItemViewSelector.empty();
    }

    private ItemViewArg(ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }

    public void select(int position, T item) {
        selector.select(itemView, position, item);
    }

    public int bindingVariable() {
        return itemView.bindingVariable();
    }

    public int layoutRes() {
        return itemView.layoutRes();
    }

    public int viewTypeCount() {
        return selector.viewTypeCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemViewArg<?> that = (ItemViewArg<?>) o;

        if (!itemView.equals(that.itemView)) return false;
        return selector == that.selector;
    }

    @Override
    public int hashCode() {
        int result = itemView.hashCode();
        result = 31 * result + selector.hashCode();
        return result;
    }
}
