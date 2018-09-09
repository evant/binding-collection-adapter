package me.tatarka.bindingcollectionadapter2.itembindings;

import androidx.annotation.NonNull;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Implement this interface on yor items to use with {@link OnItemBindModel}.
 */
public interface ItemBindingModel {
    /**
     * Set the binding variable and layout of the given view.
     * <pre>{@code
     * onItemBind.set(BR.item, R.layout.item);
     * }</pre>
     */
    void onItemBind(@NonNull ItemBinding itemBinding);
}
