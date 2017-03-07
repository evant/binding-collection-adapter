package me.tatarka.bindingcollectionadapter2.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BR;
import me.tatarka.bindingcollectionadapter2.BindingCollectionAdapter;
import me.tatarka.bindingcollectionadapter2.recyclerview.test.R;

import static me.tatarka.bindingcollectionadapter2.recyclerview.test.R.layout.item;
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
    public void dynamicItemViewInRecyclerView() {
        List<String> items = Arrays.asList("one", "two", "three");
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemBinding.<String>of(BR.item, item));

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view, null, false);
        binding.setVariable(BR.viewModel, viewModel);
        binding.executePendingBindings();

        TestHelpers.ViewModel newViewModel = new TestHelpers.ViewModel(items, ItemBinding.<String>of(BR.item, R.layout.item2));
        binding.setVariable(BR.viewModel, newViewModel);
        binding.executePendingBindings();

        RecyclerView recyclerView = (RecyclerView) binding.getRoot();
        @SuppressWarnings("unchecked")
        BindingCollectionAdapter<String> adapter = (BindingCollectionAdapter<String>) recyclerView.getAdapter();

        assertThat(adapter.getItemBinding().layoutRes()).isEqualTo(R.layout.item2);
    }

    @Test
    @UiThreadTest
    public void adapterDoesntChangeForSameItemBindingInRecylerView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, ItemBinding.<String>of(BR.item, item));

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
    public void adapterDoesntChangeForSameItemBindingSelectorInRecyclerView() {
        ObservableList<String> items = new ObservableArrayList<>();
        items.addAll(Arrays.asList("one", "two", "three"));
        ItemBinding<String> itemBinding = ItemBinding.of(new OnItemBind<String>() {
            @Override
            public void onItemBind(ItemBinding itemBinding, int position, String item) {
                itemBinding.set(BR.item, position);
            }
        });
        TestHelpers.ViewModel viewModel = new TestHelpers.ViewModel(items, itemBinding);

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
