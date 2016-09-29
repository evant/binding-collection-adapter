package me.tatarka.bindingcollectionadapter.sample.fragmentitem;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

import me.tatarka.bindingcollectionadapter.viewmodels.FragmentItemViewModel;

/**
 * Created by wanghaiming on 2016/9/21.
 */
public class FragmentViewPagerViewModel {
    private ObservableList<FragmentItemViewModel> mFragmentItemList = new ObservableArrayList<>();
    private FragmentManager mFragmentManager;

    private View.OnClickListener mOnClickListener;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public ObservableList<FragmentItemViewModel> getFragmentItemList() {
        return mFragmentItemList;
    }

    public void setFragmentItemList(ObservableList<FragmentItemViewModel> fragmentItemList) {
        mFragmentItemList = fragmentItemList;
    }
    public void addItem(FragmentItemViewModel item){
        mFragmentItemList.add(item);
    }
    public void addItems(List<FragmentItemViewModel> items){
        mFragmentItemList.addAll(items);
    }

}
