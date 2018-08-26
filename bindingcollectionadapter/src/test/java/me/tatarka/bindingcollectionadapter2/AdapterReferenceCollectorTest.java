package me.tatarka.bindingcollectionadapter2;

import androidx.databinding.ObservableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.ref.WeakReference;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class AdapterReferenceCollectorTest {

    @Test
    public void removesCallbackWhenAdapterIsCollected() throws Exception {
        BindingCollectionAdapter adapter = mock(BindingCollectionAdapter.class);
        ObservableList items = mock(ObservableList.class);
        ObservableList.OnListChangedCallback callback = mock(ObservableList.OnListChangedCallback.class);
        WeakReference ref = AdapterReferenceCollector.createRef(adapter, items, callback);
        adapter = null;
        System.gc();
        do {
            Thread.sleep(50);
        } while (ref.get() != null);
        verify(items).removeOnListChangedCallback(callback);
    }
}
