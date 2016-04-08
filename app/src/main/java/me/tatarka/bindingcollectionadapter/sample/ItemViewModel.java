package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public class ItemViewModel extends BaseObservable {
    public final boolean checkable;
    @Bindable
    private boolean checked;

    @Bindable
    protected String name;

    public ItemViewModel() {
        this.checkable = false;
    }

    public ItemViewModel(int index, boolean checkable) {
        this.name = index+" a item";
        this.checkable = checkable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }
    
    public boolean onToggleChecked(View v) {
        if (!checkable) {
            return false;
        }
        checked = !checked;
        notifyPropertyChanged(BR.checked);
        return true;
    }
}
