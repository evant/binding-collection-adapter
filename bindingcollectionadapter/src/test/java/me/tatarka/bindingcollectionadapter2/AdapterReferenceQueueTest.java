package me.tatarka.bindingcollectionadapter2;

import android.databinding.ObservableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdapterReferenceQueueTest {
    @Mock
    private ObservableList.OnListChangedCallback<ObservableList<Object>> callback;
    @Mock
    private ObservableList<Object> items;

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void expungeStaleCallbacks() throws Exception {
        AdapterReferenceQueue queue = new AdapterReferenceQueue();
        BindingCollectionAdapter<Object> adapter = new BindingViewPagerAdapter<>();
        AdapterReference<BindingCollectionAdapter<Object>, Object> ref = new AdapterReference<>(adapter, queue, callback);
        ref.setItems(items);

        adapter = null;
        System.gc();
        do {
            Thread.sleep(50);
        } while (ref.get() != null);
        queue.expungeStaleCallbacks();

        verify(items).removeOnListChangedCallback(callback);
    }
}
