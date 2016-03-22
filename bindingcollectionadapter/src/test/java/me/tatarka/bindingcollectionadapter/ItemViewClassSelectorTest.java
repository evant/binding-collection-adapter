package me.tatarka.bindingcollectionadapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class ItemViewClassSelectorTest {

    @Test
    public void selectsBasedOnClass() {
        ItemViewClassSelector<Object> selector = ItemViewClassSelector.builder()
                .put(String.class, 0, 1)
                .put(Integer.class, 2, 3)
                .build();
        ItemView itemView = new ItemView();
        List<Object> list = Arrays.<Object>asList("one", 2);
        selector.select(itemView, 0, list.get(0));

        assertThat(itemView.bindingVariable()).isEqualTo(0);
        assertThat(itemView.layoutRes()).isEqualTo(1);

        selector.select(itemView, 1, list.get(1));

        assertThat(itemView.bindingVariable()).isEqualTo(2);
        assertThat(itemView.layoutRes()).isEqualTo(3);

        try {
            selector.select(itemView, 3, new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
