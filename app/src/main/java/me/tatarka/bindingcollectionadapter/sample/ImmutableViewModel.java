package me.tatarka.bindingcollectionadapter.sample;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass;

public class ImmutableViewModel extends ViewModel implements ImmutableListeners {

    private final MutableLiveData<List<ImmutableItem>> list = new MutableLiveData<>();
    private final LiveData<List<Object>> headerFooterList = Transformations.map(list, input -> {
        List<Object> list = new ArrayList<>(input.size() + 2);
        list.add("Header");
        list.addAll(input);
        list.add("Footer");
        return list;
    });

    public final ItemBinding<Object> multipleItems = ItemBinding.of(new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.item_header_footer)
            .map(ImmutableItem.class, BR.item, R.layout.item_immutable))
            .bindExtra(BR.listeners, this);

    public final DiffUtil.ItemCallback<Object> diff = new DiffUtil.ItemCallback<Object>() {
        @Override
        public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            if (oldItem instanceof ImmutableItem && newItem instanceof ImmutableItem) {
                return ((ImmutableItem) oldItem).index == ((ImmutableItem) newItem).index;
            }
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ImmutableViewModel() {
        ArrayList<ImmutableItem> items = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            items.add(new ImmutableItem(i, false));
        }
        list.setValue(items);
    }

    public LiveData<List<Object>> getList() {
        return headerFooterList;
    }

    @Override
    public boolean onToggleChecked(int index) {
        ArrayList<ImmutableItem> newList = new ArrayList<>(list.getValue());
        ImmutableItem item = newList.get(index);
        newList.set(index, item.withChecked(!item.checked));
        list.setValue(newList);
        return true;
    }

    @Override
    public void onAddItem() {
        ArrayList<ImmutableItem> newList = new ArrayList<>(list.getValue());
        newList.add(new ImmutableItem(newList.size(), false));
        list.setValue(newList);
    }

    @Override
    public void onRemoveItem() {
        if (list.getValue().size() > 1) {
            ArrayList<ImmutableItem> newList = new ArrayList<>(list.getValue());
            newList.remove(newList.size() - 1);
            list.setValue(newList);
        }
    }
}
