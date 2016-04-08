package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public abstract class ItemViewModel extends BaseObservable {
    public final boolean checkable;
    protected String text;

    public ItemViewModel(String text) {
        this.checkable = false;
        this.text = text;
    }

    public ItemViewModel(boolean checkable, String text) {
        this.checkable = checkable;
        this.text = text;
    }

    @Bindable
    public String getName() {
        return text;
    }

    @Bindable
    public String getHeader() {
        return text;
    }

    @Bindable
    public boolean isChecked() {
        return false;
    }

    public boolean onToggleChecked(View v) {
        return false;
    }
}
