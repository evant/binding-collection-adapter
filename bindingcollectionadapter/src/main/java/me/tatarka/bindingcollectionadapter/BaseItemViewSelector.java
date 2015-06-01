package me.tatarka.bindingcollectionadapter;

/**
 * Created by evantatarka on 5/26/15.
 */
public abstract class BaseItemViewSelector<T> implements ItemViewSelector<T> {
    private static final ItemViewSelector EMPTY = new BaseItemViewSelector() {
        @Override
        public void select(ItemView itemView, int position, Object item) {
            
        }
    };
    
    @SuppressWarnings("unchecked")
    public static <T> ItemViewSelector<T> empty() {
        return EMPTY;
    }
    
    
    @Override
    public int count() {
        return 1;
    }
}
