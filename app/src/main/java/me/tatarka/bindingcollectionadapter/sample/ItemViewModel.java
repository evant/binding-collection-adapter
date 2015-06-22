package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by evan on 6/14/15.
 */
public class ItemViewModel extends BaseObservable {
    @Bindable
    private int index;
    @Bindable
    private boolean checked;

    public ItemViewModel(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean checked() {
        return checked;
    }

    public final View.OnClickListener onToggleChecked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checked = !checked;
            notifyPropertyChanged(BR.checked);
        }
    };
}
