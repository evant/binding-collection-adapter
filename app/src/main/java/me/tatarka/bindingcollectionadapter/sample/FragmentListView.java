package me.tatarka.bindingcollectionadapter.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.BindingFragmentViewPagerAdapter;
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
//        setRetainInstance(true);
        Bundle bundle = getArguments();
        if((bundle != null)&&(bundle.getSerializable(BindingFragmentViewPagerAdapter.KEY_ARG_INPUT) != null)){
            viewModel = new ViewModel(true,(Integer) bundle.getSerializable(BindingFragmentViewPagerAdapter.KEY_ARG_INPUT));
        }
        else{
            viewModel = new ViewModel(true);
        }
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
