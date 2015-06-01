package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by evan on 5/16/15.
 */
public class Stuff extends BaseObservable {
    @Bindable
    public final ObservableList<String> things = new ObservableArrayList<>();
    @Bindable
    private boolean loading;

    public Stuff() {
        for (int i = 0; i < 50; i++) {
            things.add("Thing " + i);
        }
    }

    public void addThing() {
        try {
            things.add("Thing " + things.size());
        } catch (IllegalStateException e) {
            // Bug where release() gets called twice
        }
    }

    public void removeThing() {
        if (things.size() == 0) {
            return;
        }
        try {
            things.remove(things.size() - 1);
        } catch (IllegalStateException e) {
            // Bug where release() gets called twice
        }
    }

    public int size() {
        return things.size();
    }

    public ItemView getItemView() {
        return ItemView.of(BR.thing, R.layout.item_thing);
    }

    public boolean getLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }
}
