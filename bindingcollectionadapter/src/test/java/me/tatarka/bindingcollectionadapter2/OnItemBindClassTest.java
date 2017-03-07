package me.tatarka.bindingcollectionadapter2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class OnItemBindClassTest {

    @Test
    public void selectsBasedOnClass() {
        OnItemBindClass<Object> onItemBind = new OnItemBindClass<>()
                .map(String.class, 0, 1)
                .map(Integer.class, 2, 3);
        ItemBinding<Object> itemBinding = ItemBinding.of(onItemBind);
        List<Object> list = Arrays.<Object>asList("one", 2);
        itemBinding.onItemBind(0, list.get(0));

        assertThat(itemBinding.variableId()).isEqualTo(0);
        assertThat(itemBinding.layoutRes()).isEqualTo(1);

        itemBinding.onItemBind(1, list.get(1));

        assertThat(itemBinding.variableId()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);

        try {
            itemBinding.onItemBind(3, new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void selectsBasedOnSubclass() {
        OnItemBindClass<Object> onItemBind = new OnItemBindClass<>()
                .map(A.class, 0, 1)
                .map(C.class, 2, 3);
        ItemBinding<Object> itemBinding = ItemBinding.of(onItemBind);
        List<Object> list = Arrays.asList(new B(), new C());
        itemBinding.onItemBind(0, list.get(0));

        assertThat(itemBinding.variableId()).isEqualTo(0);
        assertThat(itemBinding.layoutRes()).isEqualTo(1);

        itemBinding.onItemBind(1, list.get(1));

        assertThat(itemBinding.variableId()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);
    }

    private static class A {
    }

    private static class B extends A {
    }

    private static class C {
    }
}
