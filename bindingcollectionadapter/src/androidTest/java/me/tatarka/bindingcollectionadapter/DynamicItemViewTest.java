package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.test.R;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(AndroidJUnit4.class)
public class DynamicItemViewTest {

    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class);

    private LayoutInflater inflater;

    @Before
    public void setup() throws Exception {
        inflater = LayoutInflater.from(activityTestRule.getActivity());
    }

    @Test
    @UiThreadTest
    public void dynamicItemViewInListView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item)).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item2)).build();
        binding.setVariable(BR.viewModel, newViewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) listView.getAdapter();

        assertThat(adapter.getItemViewArg().layoutRes()).isEqualTo(R.layout.item2);
    }

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemViewInListView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item)).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView1 = (ListView) binding.getRoot();
        BindingCollectionAdapter<String> adapter1 = (BindingCollectionAdapter<String>) listView1.getAdapter();

        items.add("four");

        ListView listView2 = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter2 = (BindingCollectionAdapter<String>) listView2.getAdapter();

        assertThat(adapter1).isSameAs(adapter2);
    }

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemViewSelectorInListView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        ItemViewSelector<String> itemView = new BaseItemViewSelector<String>() {
            @Override
            public void select(ItemView itemView, int position, String item) {
                itemView.set(BR.item, R.layout.item);
            }
        };
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, itemView).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ListView listView1 = (ListView) binding.getRoot();
        BindingCollectionAdapter<String> adapter1 = (BindingCollectionAdapter<String>) listView1.getAdapter();

        items.add("four");

        ListView listView2 = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter2 = (BindingCollectionAdapter<String>) listView2.getAdapter();

        assertThat(adapter1).isSameAs(adapter2);
    }

    @Test
    @UiThreadTest
    public void dynamicItemViewInViewPager() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item)).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item2)).build();
        binding.setVariable(BR.viewModel, newViewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) viewPager.getAdapter();

        assertThat(adapter.getItemViewArg().layoutRes()).isEqualTo(R.layout.item2);
    }
}
