package me.tatarka.bindingcollectionadapter2;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.test.R;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class ListViewInflationTest {

    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class);

    private LayoutInflater inflater;

    @Before
    public void setup() throws Exception {
        inflater = LayoutInflater.from(activityTestRule.getActivity());
    }

    @Test
    @UiThreadTest
    public void listView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @Test
    @UiThreadTest
    public void listViewSelector() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_selector, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @Test
    @UiThreadTest
    public void listViewItemIds() {
        List<String> items = Arrays.asList("one", "two", "three");
        List<Long> itemIds = Arrays.asList(1L, 2L, 3L);
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).itemIds(itemIds).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_id, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterableIds(adapter)).containsExactlyElementsOf(itemIds);
    }

    @Test
    @UiThreadTest
    public void listViewItemIsEnabled() {
        List<String> items = Arrays.asList("one", "two", "three");
        List<Boolean> itemIsEnabled = Arrays.asList(true, true, false);
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).itemIsEnabled(itemIsEnabled).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_is_enabled, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(TestHelpers.iterableIsEnabled(adapter)).containsExactlyElementsOf(itemIsEnabled);
    }

    @Test
    @UiThreadTest
    public void listViewAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_adapter, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingListViewAdapter.class);
    }

    @Test
    @UiThreadTest
    public void listViewAdapterItemIds() {
        List<String> items = Arrays.asList("one", "two", "three");
        List<Long> itemIds = Arrays.asList(1L, 2L, 3L);
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).itemIds(itemIds).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view_adapter_id, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) listView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingListViewAdapter.class);
        assertThat(TestHelpers.iterableIds(adapter)).containsExactlyElementsOf(itemIds);
    }

    @Test
    @UiThreadTest
    public void listViewHeaderAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();
        // addHeaderView must be called after the adapter is set.
        ((ListView) binding.getRoot()).addHeaderView(new View(binding.getRoot().getContext()));
        // Trigger rebind to exercise the case when the adapter it wrapped in a HeaderListViewAdapter
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingListViewAdapter<String> adapter = (BindingListViewAdapter<String>) ((WrapperListAdapter) listView.getAdapter()).getWrappedAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }
}