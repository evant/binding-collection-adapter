package me.tatarka.bindingcollectionadapter.sample;

import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public class NormalItemViewModel extends ItemViewModel {
    public final boolean checkable;
    private boolean checked;

    public NormalItemViewModel(int index, boolean checkable) {
        super(index+" a item");
        this.checkable = checkable;
    }

    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public boolean onToggleChecked(View v) {
        if (!checkable) {
            return false;
        }
        checked = !checked;
        notifyPropertyChanged(BR.checked);
        return true;
    }
}
