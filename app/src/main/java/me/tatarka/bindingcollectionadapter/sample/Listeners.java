package me.tatarka.bindingcollectionadapter.sample;

/**
 * Created by evan on 6/3/15.
 */
public class Listeners {
    private ViewModel viewModel;

    public Listeners(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
   
    public void onAddItem() {
        viewModel.addItem();
    }

    public void onRemoveItem() {
        viewModel.removeItem();
    }
}
