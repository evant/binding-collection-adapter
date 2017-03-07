package me.tatarka.bindingcollectionadapter2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.itembindings.ItemBindingModel;
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindModel;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class OnItemBindModelTest {

    @Test
    public void selectsBasedOnItem() {
        OnItemBindModel<ItemBindingModel> onItemBind = new OnItemBindModel<>();
        ItemBinding<ItemBindingModel> itemBinding = ItemBinding.of(onItemBind);
        List<ItemBindingModel> list = Arrays.asList(new ItemBindingModelOne(), new ItemBindingModelTwo());
        itemBinding.onItemBind(0, list.get(0));
        
        assertThat(itemBinding.variableId()).isEqualTo(0);
        assertThat(itemBinding.layoutRes()).isEqualTo(1);
        
        itemBinding.onItemBind(1, list.get(1));
        assertThat(itemBinding.variableId()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);
    }

    public static class ItemBindingModelOne implements ItemBindingModel {
        @Override
        public void onItemBind(ItemBinding itemBinding) {
            itemBinding.set(0, 1);
        }
    }

    public static class ItemBindingModelTwo implements ItemBindingModel {
        @Override
        public void onItemBind(ItemBinding itemBinding) {
            itemBinding.set(2, 3);
        }
    }
}
