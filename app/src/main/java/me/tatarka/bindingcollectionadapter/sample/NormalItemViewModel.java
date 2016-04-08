package me.tatarka.bindingcollectionadapter.sample;

import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public class NormalItemViewModel extends ItemViewModel {

    private boolean checked;

    public NormalItemViewModel(int index, boolean checkable) {
        super(checkable, index+" a item");
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
