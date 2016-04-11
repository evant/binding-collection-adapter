package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.sample.databinding.ListViewBinding;

/**
 * Created by evan on 5/31/15.
 */
public class FragmentListView extends Fragment {
    private static final String TAG = "BindingList";
    private ViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ObservableList<ItemViewModel> items = new ObservableArrayList<>();
        items.add(new HeaderItemViewModel(getString(R.string.header)));
        for (int i = 0; i < 3; i++) {
            items.add(new NormalItemViewModel(i, true));
        }

        viewModel = new ViewModel(true, items);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListViewBinding binding = ListViewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setListeners(new Listeners(viewModel));
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
