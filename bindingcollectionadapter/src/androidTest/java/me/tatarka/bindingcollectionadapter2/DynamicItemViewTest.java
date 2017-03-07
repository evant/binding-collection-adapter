package me.tatarka.bindingcollectionadapter2;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.test.R;

import static me.tatarka.bindingcollectionadapter2.test.R.layout.item;
import static org.assertj.core.api.Assertions.assertThat;

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
    public void dynamicItemBindingInListView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(BR.item, item)).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(BR.item, R.layout.item2)).build();
        binding.setVariable(BR.viewModel, newViewModel);
        binding.executePendingBindings();

        ListView listView = (ListView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) listView.getAdapter();

        assertThat(adapter.getItemBinding().layoutRes()).isEqualTo(R.layout.item2);
    }

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemBindingInListView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(BR.item, item)).build();

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
    public void adapterDoesntChangeForSameItemBindingSelectorInListView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        ItemBinding<String> itemBinding = ItemBinding.of(new OnItemBind<String>() {
            @Override
            public void onItemBind(ItemBinding itemBinding, int position, String item) {
                itemBinding.set(me.tatarka.bindingcollectionadapter2.BR.item, position);
            }
        });
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, itemBinding).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_view, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
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
    public void dynamicItemBindingInViewPager() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, item)).build();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_adapter, null, false);
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel.Builder(items, ItemBinding.<String>of(me.tatarka.bindingcollectionadapter2.BR.item, R.layout.item2)).build();
        binding.setVariable(me.tatarka.bindingcollectionadapter2.BR.viewModel, newViewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) viewPager.getAdapter();

        assertThat(adapter.getItemBinding().layoutRes()).isEqualTo(R.layout.item2);
    }
}
