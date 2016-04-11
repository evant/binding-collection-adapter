package me.tatarka.bindingcollectionadapter.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.sample.databinding.SpinnerViewBinding;

/**
 * Created by evan on 5/31/15.
 */
public class FragmentSpinnerView extends Fragment {
    private static final String TAG = "BindingSpinner";
    private ViewModel viewModel;
    SpinnerViewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        viewModel = new ViewModel(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SpinnerViewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.setViewModel(viewModel);
        binding.setListeners(new Listeners(viewModel));
        binding.executePendingBindings();
    }
}
