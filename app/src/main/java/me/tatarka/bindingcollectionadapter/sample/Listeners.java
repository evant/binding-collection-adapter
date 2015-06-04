package me.tatarka.bindingcollectionadapter.sample;

import android.view.View;

/**
 * Created by evan on 6/3/15.
 */
public class Listeners {
    private ViewModel viewModel;
    
    public Listeners(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    public final View.OnClickListener addItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddItem();
        }
    };
    
    public final View.OnClickListener removeItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRemoveItem();
        }
    };
    
    public void onAddItem() {
        viewModel.addItem();
    }
    
    public void onRemoveItem() {
        viewModel.removeItem();
    }
}
