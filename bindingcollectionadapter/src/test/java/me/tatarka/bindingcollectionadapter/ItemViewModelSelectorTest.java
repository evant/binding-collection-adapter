package me.tatarka.bindingcollectionadapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModelSelector;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ItemViewModelSelectorTest {

    @Test
    public void selectsBasedOnItem() {
        ItemViewModelSelector<ItemViewModel> selector = new ItemViewModelSelector<>();
        ItemView itemView = new ItemView();
        List<ItemViewModel> list = Arrays.asList(new ItemViewModelOne(), new ItemViewModelTwo());
        selector.select(itemView, 0, list.get(0));
        
        assertThat(itemView.bindingVariable()).isEqualTo(0);
        assertThat(itemView.layoutRes()).isEqualTo(1);
        
        selector.select(itemView, 1, list.get(1));
        assertThat(itemView.bindingVariable()).isEqualTo(2);
        assertThat(itemView.layoutRes()).isEqualTo(3);
    }

    public static class ItemViewModelOne implements ItemViewModel {
        @Override
        public void itemView(ItemView itemView) {
            itemView.set(0, 1);
        }
    }

    public static class ItemViewModelTwo implements ItemViewModel {
        @Override
        public void itemView(ItemView itemView) {
            itemView.set(2, 3);
        }
    }
}
