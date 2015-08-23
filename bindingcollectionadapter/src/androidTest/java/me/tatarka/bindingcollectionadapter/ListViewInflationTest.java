package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.test.BR;
import me.tatarka.bindingcollectionadapter.test.R;

import static org.assertj.core.api.Assertions.assertThat;

public class ListViewInflationTest extends InstrumentationTestCase {

    private LayoutInflater inflater;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        inflater = LayoutInflater.from(getInstrumentation().getContext());
    }

    @UiThreadTest
    public void testListView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @UiThreadTest
    public void testListViewSelector() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_selector, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @UiThreadTest
    public void testListViewItemIds() {
        List<String> items = Arrays.asList("one", "two", "three");
        List<Long> itemIds = Arrays.asList(1L, 2L, 3L);
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item), itemIds);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_id, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterableIds(adapter)).containsExactlyElementsOf(itemIds);
    }

    @UiThreadTest
    public void testListViewAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingListViewAdapter.class);
    }

    @UiThreadTest
    public void testListViewAdapterFactory() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_factory_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingListViewAdapter.class);
    }

    @UiThreadTest
    public void testListViewAdapterItemIds() {
        List<String> items = Arrays.asList("one", "two", "three");
        List<Long> itemIds = Arrays.asList(1L, 2L, 3L);
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item), itemIds);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_adapter_id, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingListViewAdapter.class);
        assertThat(TestHelpers.iterableIds(adapter)).containsExactlyElementsOf(itemIds);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}