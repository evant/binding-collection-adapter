package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.BaseBindingCollectionListener;
import me.tatarka.bindingcollectionadapter.BindingCollectionAdapter;
import me.tatarka.bindingcollectionadapter.sample.databinding.ListViewBinding;
import me.tatarka.bindingcollectionadapter.sample.databinding.RecyclerViewBinding;

/**
 * Created by evan on 5/31/15.
 */
public class FragmentRecyclerView extends Fragment {
    private static final String TAG = "BindingRecyclerView";
    private static final ViewModel viewModel = new ViewModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerViewBinding binding = RecyclerViewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setListeners(new Listeners(viewModel));
        binding.executePendingBindings();
        ((BindingCollectionAdapter<ItemViewModel>) binding.list.getAdapter()).setBindingCollectionListener(new BaseBindingCollectionListener<ItemViewModel>() {
            @Override
            public void onBindingCreated(ViewDataBinding binding) {
                Log.d(TAG, "created binding: " + binding);
            }

            @Override
            public void onBindingBound(ViewDataBinding binding, int position, ItemViewModel item) {
                Log.d(TAG, "bound binding: " + binding + " to position: " + position);
            }
        });
        return binding.getRoot();
    }
}
