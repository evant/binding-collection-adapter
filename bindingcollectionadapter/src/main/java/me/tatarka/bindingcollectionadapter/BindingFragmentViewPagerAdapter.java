package me.tatarka.bindingcollectionadapter;

import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import me.tatarka.bindingcollectionadapter.viewmodels.FragmentItemViewModel;

/**
 * Created by wanghaiming on 2016/9/21.
 */
public class BindingFragmentViewPagerAdapter<T extends FragmentItemViewModel> extends FragmentPagerAdapter implements BindingCollectionAdapter<T> {

    public static final String KEY_ARG_INPUT = "key_arg_input";
    public static final String KEY_DATA_ATTACHED = "key_data_attached";
    private List<T> mItems;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);

    // only for re-implement destroyItem
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public BindingFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    /*BindingCollectionAdapter*/
    @Override
    public ItemViewArg<T> getItemViewArg() {
        return null;
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.mItems == items) {
            return;
        }
        if (this.mItems instanceof ObservableList) {
            ((ObservableList<T>) this.mItems).removeOnListChangedCallback(callback);
        }
        if (items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return mItems.get(position);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, int position, T item) {

    }

    /*FragmentPagerAdapter*/

    @Override
    public CharSequence getPageTitle(int position) {
        T itemData = mItems.get(position);
        return itemData.getFragTitle();
    }

    @Override
    public Fragment getItem(int position) {
        T itemData = mItems.get(position);
        Fragment fragment = null;
        try {
            fragment = itemData.getFragClass().newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_DATA_ATTACHED,itemData);

            if(itemData.getFragArg() != null){
                bundle.putSerializable(KEY_ARG_INPUT,itemData.getFragArg());
            }

            fragment.setArguments(bundle);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        T itemData = (T)((Fragment)object).getArguments().getSerializable(KEY_DATA_ATTACHED);
        if(mItems.contains(itemData)){
            super.destroyItem(container,position,object);
        }
        else{
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.remove((Fragment)object);
        }

    }

    @Override
    public int getItemPosition(Object object) {
        T itemData = (T)((Fragment)object).getArguments().getSerializable(KEY_DATA_ATTACHED);
        if(!mItems.contains(itemData)){
            return POSITION_NONE;
        }
        else {
            return mItems.indexOf(itemData);
        }
    }

    /* if no this funciton, the destory item. ft.remove will not run.*/
    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);

        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    /*fragmentPagerAdaper结束*/
    private static class WeakReferenceOnListChangedCallback<T extends FragmentItemViewModel> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingFragmentViewPagerAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingFragmentViewPagerAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingFragmentViewPagerAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }
    }


}
