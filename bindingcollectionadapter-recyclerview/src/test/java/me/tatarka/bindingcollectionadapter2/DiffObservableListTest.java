package me.tatarka.bindingcollectionadapter2;

import android.databinding.ObservableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class DiffObservableListTest {

    @Test
    public void insetOneItem() {
        DiffObservableList<Item> list = new DiffObservableList<>(Item.DIFF_CALLBACK);
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.update(Arrays.asList(new Item("1", "a")));

        assertThat(list)
                .hasSize(1)
                .containsExactly(new Item("1", "a"));
        verify(callback).onItemRangeInserted(list, 0, 1);
    }

    @Test
    public void removeOneItem() {
        DiffObservableList<Item> list = new DiffObservableList<>(Item.DIFF_CALLBACK);
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.update(Arrays.asList(new Item("1", "a")));
        list.update(Arrays.<Item>asList());

        assertThat(list).isEmpty();
        verify(callback).onItemRangeRemoved(list, 0, 1);
    }
    
    @Test
    public void moveOneItem() {
        DiffObservableList<Item> list = new DiffObservableList<>(Item.DIFF_CALLBACK);
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.update(Arrays.asList(new Item("1", "a"), new Item("2", "b")));
        list.update(Arrays.asList(new Item("2", "b"), new Item("1", "a")));

        assertThat(list)
                .hasSize(2)
                .containsExactly(new Item("2", "b"), new Item("1", "a"));
        verify(callback).onItemRangeMoved(list, 1, 0, 1);
    }
    
    @Test
    public void changeItem() {
        DiffObservableList<Item> list = new DiffObservableList<>(Item.DIFF_CALLBACK);
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.update(Arrays.asList(new Item("1", "a")));
        list.update(Arrays.asList(new Item("1", "b")));

        assertThat(list)
                .hasSize(1)
                .containsExactly(new Item("1", "b"));
        verify(callback).onItemRangeChanged(list, 0, 1);
    }
    

    static class Item {

        static final DiffObservableList.Callback<Item> DIFF_CALLBACK = new DiffObservableList.Callback<Item>() {
            @Override
            public boolean areItemsTheSame(Item oldItem, Item newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(Item oldItem, Item newItem) {
                return oldItem.value.equals(newItem.value);
            }
        };

        final String id;
        final String value;

        Item(String id, String value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            if (!id.equals(item.id)) return false;
            return value.equals(item.value);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Test(" +
                    "id='" + id + '\'' +
                    ", value='" + value + '\'' +
                    ')';
        }
    }
}
