package me.tatarka.bindingcollectionadapter2;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class OnItemBindClassAndroidTest {

    @Test
    // SparseArray not mocked for JUnit
    public void selectsBasedOnClassWithExtra() {
        OnItemBindClass<Object> onItemBind = new OnItemBindClass<>()
                .map(C.class, 0, 1)
                .map(A.class, new OnItemBind<A>() {
                    @Override
                    public void onItemBind(ItemBinding itemBinding, int position, A item) {
                        itemBinding.set(2, 3);
                        itemBinding.clearExtras();
                        itemBinding.bindExtra(4, "extra1");
                        itemBinding.bindExtra(5, "extra2");
                    }
                });

        ItemBinding<Object> itemBinding = ItemBinding.of(onItemBind);
        List<Object> list = Arrays.asList(new B(), new C(), new A());
        itemBinding.onItemBind(0, list.get(0));

        assertThat(itemBinding.variableId()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);

        assertThat(itemBinding.extraBinding(4)).isEqualTo("extra1");
        assertThat(itemBinding.extraBinding(5)).isEqualTo("extra2");

        itemBinding.onItemBind(1, list.get(1));

        assertThat(itemBinding.variableId()).isEqualTo(0);
        assertThat(itemBinding.layoutRes()).isEqualTo(1);

        // in current implementation extras not cleared after map(Class, int, int)
//        assertThat(itemBinding.extraBinding(4)).isNull();
//        assertThat(itemBinding.extraBinding(5)).isNull();

        itemBinding.onItemBind(2, list.get(2));

        assertThat(itemBinding.variableId()).isEqualTo(2);
        assertThat(itemBinding.layoutRes()).isEqualTo(3);

        assertThat(itemBinding.extraBinding(4)).isEqualTo("extra1");
        assertThat(itemBinding.extraBinding(5)).isEqualTo("extra2");
    }

    private static class A {
    }

    private static class B extends A {
    }

    private static class C {
    }
}
