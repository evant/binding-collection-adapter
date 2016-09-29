package me.tatarka.bindingcollectionadapter.factories;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import me.tatarka.bindingcollectionadapter.BindingFragmentViewPagerAdapter;
import me.tatarka.bindingcollectionadapter.viewmodels.FragmentItemViewModel;

public interface BindingFragmentViewPagerAdapterFactory {
    <T extends FragmentItemViewModel> BindingFragmentViewPagerAdapter<T> create(ViewPager viewPager, FragmentManager fm);

    BindingFragmentViewPagerAdapterFactory DEFAULT = new BindingFragmentViewPagerAdapterFactory() {
        @Override
        public <T extends FragmentItemViewModel> BindingFragmentViewPagerAdapter<T> create(ViewPager viewPager, FragmentManager fm) {
            return new BindingFragmentViewPagerAdapter<>(fm);
        }
    };
}
