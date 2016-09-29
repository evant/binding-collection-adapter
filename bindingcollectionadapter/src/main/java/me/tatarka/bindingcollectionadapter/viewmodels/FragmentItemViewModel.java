package me.tatarka.bindingcollectionadapter.viewmodels;

import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by wanghaiming on 2016/9/21.
 */
public class FragmentItemViewModel implements Serializable {
    private Serializable mFragArg;
    private Class<? extends Fragment>     mFragClass;
    private CharSequence mFragTitle;

    public FragmentItemViewModel(Serializable fragArg, Class<? extends Fragment> fragClass, CharSequence fragTitle) {
        mFragArg = fragArg;
        mFragClass = fragClass;
        mFragTitle = fragTitle;
    }

    public Serializable getFragArg() {
        return mFragArg;
    }

    public void setFragArg(Serializable fragArg) {
        mFragArg = fragArg;
    }

    public Class<? extends Fragment> getFragClass() {
        return mFragClass;
    }

    public void setFragClass(Class<? extends Fragment> fragClass) {
        mFragClass = fragClass;
    }

    public CharSequence getFragTitle() {
        return mFragTitle;
    }

    public void setFragTitle(CharSequence fragTitle) {
        mFragTitle = fragTitle;
    }
}
