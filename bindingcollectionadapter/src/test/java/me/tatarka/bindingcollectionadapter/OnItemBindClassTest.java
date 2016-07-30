package me.tatarka.bindingcollectionadapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.itembindings.OnItemBindClass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class OnItemBindClassTest {

    @Test
    public void selectsBasedOnClass() {
        OnItemBindClass<Object> selector = OnItemBindClass.builder()
                .put(String.class, 0, 1)
                .put(Integer.class, 2, 3)
                .build();
        ItemView itemBinding = new ItemView();
        List<Object> list = Arrays.<Object>asList("one", 2);
        selector.select(itemBinding, 0, list.get(0));

        assertThat(itemBinding.bindingVariable()).isEqualTo(0);
        assertThat(itemBinding.layoutRes()).isEqualTo(1);

        selector.select(itemBinding, 1, list.get(1));

        assertThat(itemBinding.bindingVariable()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);

        try {
            selector.select(itemBinding, 3, new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
