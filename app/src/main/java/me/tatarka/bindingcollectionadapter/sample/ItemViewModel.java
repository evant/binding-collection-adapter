package me.tatarka.bindingcollectionadapter.sample;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;

public class ItemViewModel {
    public boolean checkable;
    private int index;
    private final MutableLiveData<Boolean> checked;

    public ItemViewModel(int index) {
        this.index = index;
        checked = new MutableLiveData<>();
        checked.setValue(false);
    }

    public int getIndex() {
        return index;
    }

    public LiveData<Boolean> isChecked() {
        return checked;
    }

    public void setChecked(boolean value) {
        if (!checkable) {
            return;
        }
        checked.setValue(value);
    }

    @MainThread
    public boolean onToggleChecked() {
        if (!checkable) {
            return false;
        }
        checked.setValue(!checked.getValue());
        return true;
    }
}
