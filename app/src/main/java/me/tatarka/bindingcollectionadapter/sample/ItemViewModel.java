package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import me.tatarka.bindingcollectionadapter.sample.BR;

/**
 * Created by evan on 6/14/15.
 */
public class ItemViewModel extends BaseObservable {
    public final boolean checkable;
    @Bindable
    private int index;
    @Bindable
    private boolean checked;

    public ItemViewModel(int index, boolean checkable) {
        this.index = index;
        this.checkable = checkable;
    }

    public int getIndex() {
        return index;
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
