package me.tatarka.bindingcollectionadapter.sample;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.sample.databinding.RecyclerViewBinding;

/**
 * Created by evan on 5/31/15.
 */
public class FragmentRecyclerView extends Fragment {
    private static final String TAG = "BindingRecyclerView";
    private MyViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        viewModel.setCheckable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerViewBinding binding = RecyclerViewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListeners(new Listeners(viewModel));
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
