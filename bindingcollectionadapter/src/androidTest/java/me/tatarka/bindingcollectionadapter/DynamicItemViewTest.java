package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.v4.view.ViewPager;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.test.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicItemViewTest extends InstrumentationTestCase {

    private LayoutInflater inflater;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        inflater = LayoutInflater.from(getInstrumentation().getContext());
    }

    @UiThreadTest
    public void testDynamicItemViewInListView() {
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
    
    @UiThreadTest
    public void testAdapterDoesntChangeForSameItemViewInListView() {
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

    @UiThreadTest
    public void testAdapterDoesntChangeForSameItemViewSelectorInListView() {
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

    @UiThreadTest
    public void testDynamicItemViewInViewPager() {
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
