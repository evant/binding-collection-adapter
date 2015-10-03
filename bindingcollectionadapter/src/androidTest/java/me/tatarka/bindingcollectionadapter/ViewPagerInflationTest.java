package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.v4.view.ViewPager;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.view.LayoutInflater;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.test.R;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewPagerInflationTest extends InstrumentationTestCase {

    private LayoutInflater inflater;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        inflater = LayoutInflater.from(getInstrumentation().getContext());
    }

    @UiThreadTest
    public void testRecyclerView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingViewPagerAdapter<String> adapter = (BindingViewPagerAdapter<String>) viewPager.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @UiThreadTest
    public void testRecyclerViewAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingViewPagerAdapter<String> adapter = (BindingViewPagerAdapter<String>) viewPager.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingViewPagerAdapter.class);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}