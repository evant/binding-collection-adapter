package me.tatarka.bindingcollectionadapter2.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.recyclerview.test.R;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewInflationTest {

    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class);

    private LayoutInflater inflater;

    @Before
    public void setup() throws Exception {
        inflater = LayoutInflater.from(activityTestRule.getActivity());
    }

    @Test
    @UiThreadTest
    public void recyclerView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemBinding.<String>of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingRecyclerViewAdapter<String> adapter = (BindingRecyclerViewAdapter<String>) recyclerView.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @Test
    @UiThreadTest
    public void recyclerViewAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemBinding.<String>of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingRecyclerViewAdapter<String> adapter = (BindingRecyclerViewAdapter<String>) recyclerView.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingRecyclerViewAdapter.class);
    }
}