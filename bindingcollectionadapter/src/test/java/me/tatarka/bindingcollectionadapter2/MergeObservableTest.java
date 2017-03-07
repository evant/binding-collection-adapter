package me.tatarka.bindingcollectionadapter2;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class MergeObservableTest {
    @Test
    public void emptyListIsEmpty() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);

        assertThat(list).isEmpty();
        verifyNoMoreInteractions(callback);
    }

    @Test
    public void insertingItemContainsThatItem() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.insertItem("test");

        assertThat(list)
                .hasSize(1)
                .containsExactly("test");
        verify(callback).onItemRangeInserted(list, 0, 1);
    }

    @Test
    public void insertingListContainsThatList() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        ObservableList<String> items = new ObservableArrayList<>();
        items.add("test1");
        items.add("test2");
        list.insertList(items);

        assertThat(list)
                .hasSize(2)
                .containsExactly("test1", "test2");
        verify(callback).onItemRangeInserted(list, 0, 2);
    }

    @Test
    public void insertingItemAndListContainsItemThenList() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.insertItem("test1");
        ObservableList<String> items = new ObservableArrayList<>();
        items.add("test2");
        items.add("test3");
        list.insertList(items);

        assertThat(list)
                .hasSize(3)
                .containsExactly("test1", "test2", "test3");
        verify(callback).onItemRangeInserted(list, 0, 1);
        verify(callback).onItemRangeInserted(list, 1, 2);
    }

    @Test
    public void addingItemToBackingListAddsItemToList() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.insertItem("test1");
        ObservableList<String> items = new ObservableArrayList<>();
        items.add("test2");
        list.insertList(items);
        list.insertItem("test4");
        items.add("test3");

        assertThat(list)
                .hasSize(4)
                .containsExactly("test1", "test2", "test3", "test4");
        verify(callback).onItemRangeInserted(list, 0, 1);
        verify(callback).onItemRangeInserted(list, 1, 1);
        verify(callback, times(2)).onItemRangeInserted(list, 2, 1);
    }

    @Test
    public void removingItemFromBackingListRemovesItemFromList() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.insertItem("test1");
        ObservableList<String> items = new ObservableArrayList<>();
        items.add("test2");
        list.insertList(items);
        list.insertItem("test3");
        items.clear();

        assertThat(list)
                .hasSize(2)
                .containsExactly("test1", "test3");
        verify(callback).onItemRangeInserted(list, 0, 1);
        verify(callback).onItemRangeInserted(list, 1, 1);
        verify(callback).onItemRangeInserted(list, 2, 1);
        verify(callback).onItemRangeRemoved(list, 1, 1);
    }
    
    @Test
    public void changingItemFromBackingListChangesItInList() {
        MergeObservableList<String> list = new MergeObservableList<>();
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        list.addOnListChangedCallback(callback);
        list.insertItem("test1");
        ObservableList<String> items = new ObservableArrayList<>();
        items.add("test2");
        list.insertList(items);
        items.set(0, "test3");
        
        assertThat(list)
                .hasSize(2)
                .containsExactly("test1", "test3");
        verify(callback).onItemRangeInserted(list, 0, 1);
        verify(callback).onItemRangeInserted(list, 1, 1);
        verify(callback).onItemRangeChanged(list, 1, 1);
    }
}
