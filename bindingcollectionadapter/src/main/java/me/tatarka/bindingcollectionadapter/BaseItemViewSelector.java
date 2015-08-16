package me.tatarka.bindingcollectionadapter;

/**
 * You may extend this instead of {@link ItemViewSelector} if you do not need to implement {@link
 * #viewTypeCount()}, which is currently only used for {@link BindingListViewAdapter}.
 */
public abstract class BaseItemViewSelector<T> implements ItemViewSelector<T> {
    private static final ItemViewSelector EMPTY = new BaseItemViewSelector() {
        @Override
        public void select(ItemView itemView, int position, Object item) {

        }
    };

    /**
     * Returns an empty {@link ItemViewSelector}, i.e. one that does nothing on {@link
     * #select(ItemView, int, Object)}.
     */
    @SuppressWarnings("unchecked")
    public static <T> ItemViewSelector<T> empty() {
        return EMPTY;
    }

    @Override
    public int viewTypeCount() {
        return 1;
    }
}
