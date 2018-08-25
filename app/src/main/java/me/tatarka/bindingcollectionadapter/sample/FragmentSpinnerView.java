package me.tatarka.bindingcollectionadapter.sample;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.sample.databinding.SpinnerViewBinding;

/**
 * Created by evan on 5/31/15.
 */
public class FragmentSpinnerView extends Fragment {
    private static final String TAG = "BindingSpinner";
    private MutableViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MutableViewModel.class);
        viewModel.setCheckable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SpinnerViewBinding binding = SpinnerViewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListeners(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
