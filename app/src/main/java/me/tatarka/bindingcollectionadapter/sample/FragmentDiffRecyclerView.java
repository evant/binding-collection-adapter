package me.tatarka.bindingcollectionadapter.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import me.tatarka.bindingcollectionadapter.sample.databinding.DiffRecyclerViewBinding;

public class FragmentDiffRecyclerView extends Fragment {
    private ImmutableViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ImmutableViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DiffRecyclerViewBinding binding = DiffRecyclerViewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListeners(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
