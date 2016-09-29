package me.tatarka.bindingcollectionadapter.sample.fragmentitem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter.viewmodels.FragmentItemViewModel;
import me.tatarka.bindingcollectionadapter.sample.FragmentListView;
import me.tatarka.bindingcollectionadapter.sample.FragmentRecyclerView;
import me.tatarka.bindingcollectionadapter.sample.R;
import me.tatarka.bindingcollectionadapter.sample.databinding.FragmentViewpagerViewBinding;
import me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.view.ChildFragment;

/**
 * Created by wanghaiming on 2016/9/21.
 */
public class FragmentFragmentViewPagerView extends Fragment {
    public static final String TAG = "FragmentFragmentViewPagerView";

    private FragmentViewpagerViewBinding mBinding;
    private FragmentViewPagerViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mViewModel = new FragmentViewPagerViewModel();
        mViewModel.addItem(new FragmentItemViewModel(15,FragmentListView.class,"LIST"));
        mViewModel.addItem(new FragmentItemViewModel(null,FragmentRecyclerView.class,"RECYCLER"));
        mViewModel.addItem(new FragmentItemViewModel(null,ChildFragment.class,"CHILD"));
        mViewModel.setFragmentManager(getChildFragmentManager());
        mViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(view.getId() == R.id.add) {
                        mViewModel.addItem(new FragmentItemViewModel(null, FragmentListView.class, "LIST"));
                        mBinding.tlTabs.setTabsFromPagerAdapter(mBinding.vpViewPager.getAdapter());
                    }
                    else{
                        mViewModel.getFragmentItemList().remove(mViewModel.getFragmentItemList().size()-1);
                        mBinding.tlTabs.setTabsFromPagerAdapter(mBinding.vpViewPager.getAdapter());
                    }

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewpagerViewBinding.inflate(inflater,container,false);
        mBinding.setViewModel(mViewModel);
        mBinding.executePendingBindings();

        mBinding.tlTabs.setupWithViewPager(mBinding.vpViewPager);
        return mBinding.getRoot();
    }
}
