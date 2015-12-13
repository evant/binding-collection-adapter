package me.tatarka.bindingcollectionadapter.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.BR;
import me.tatarka.bindingcollectionadapter.BindingCollectionAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.recyclerview.test.R;

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
    public void testDynamicItemViewInRecyclerView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item2));
        binding.setVariable(BR.viewModel, newViewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) recyclerView.getAdapter();
        
        assertThat(adapter.getItemViewArg().layoutRes()).isEqualTo(R.layout.item2);
    }
}
