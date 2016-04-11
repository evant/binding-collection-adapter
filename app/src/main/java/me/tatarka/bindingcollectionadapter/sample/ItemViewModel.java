package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public abstract class ItemViewModel extends BaseObservable {
    protected String text;

    public ItemViewModel(String text) {
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

    public boolean isCheckable() {
        return false;
    }

    @Bindable
    public boolean isChecked() {
        return false;
    }

    public boolean onToggleChecked(View v) {
        return false;
    }
}
