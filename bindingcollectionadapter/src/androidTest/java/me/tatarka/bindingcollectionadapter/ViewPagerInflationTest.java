package me.tatarka.bindingcollectionadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.test.R;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class ViewPagerInflationTest {

    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class);

    private LayoutInflater inflater;

    @Before
    public void setup() throws Exception {
        inflater = LayoutInflater.from(activityTestRule.getActivity());
    }

    @Test
    @UiThreadTest
    public void testRecyclerView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingViewPagerAdapter<String> adapter = (BindingViewPagerAdapter<String>) viewPager.getAdapter();

        assertThat(TestHelpers.iterable(adapter)).containsExactlyElementsOf(items);
    }

    @Test
    @UiThreadTest
    public void testRecyclerViewAdapter() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel.Builder(items, ItemView.of(BR.item, R.layout.item)).build();
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_adapter, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        ViewPager viewPager = (ViewPager) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingViewPagerAdapter<String> adapter = (BindingViewPagerAdapter<String>) viewPager.getAdapter();

        assertThat(adapter).isInstanceOf(TestHelpers.MyBindingViewPagerAdapter.class);
    }
}