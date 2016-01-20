package me.tatarka.bindingcollectionadapter.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.BR;
import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.BindingCollectionAdapter;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.recyclerview.test.R;

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
    public void dynamicItemViewInRecyclerView() {
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

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemViewInRecylerView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemView.of(BR.item, R.layout.item));

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView1 = (RecyclerView) binding.getRoot();
        BindingCollectionAdapter<String> adapter1 = (BindingCollectionAdapter<String>) recyclerView1.getAdapter();

        items.add("four");

        RecyclerView recyclerView2 = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter2 = (BindingCollectionAdapter<String>) recyclerView2.getAdapter();

        assertThat(adapter1).isSameAs(adapter2);
    }

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemViewSelectorInRecyclerView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        ItemViewSelector<String> itemView = new BaseItemViewSelector<String>() {
            @Override
            public void select(ItemView itemView, int position, String item) {
                itemView.set(BR.item, R.layout.item);
            }
        };
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, itemView);

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView1 = (RecyclerView) binding.getRoot();
        BindingCollectionAdapter<String> adapter1 = (BindingCollectionAdapter<String>) recyclerView1.getAdapter();

        items.add("four");

        RecyclerView recyclerView2 = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter2 = (BindingCollectionAdapter<String>) recyclerView2.getAdapter();

        assertThat(adapter1).isSameAs(adapter2);
    }
}
