package me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.tatarka.bindingcollectionadapter.listeners.ItemListener;
import me.tatarka.bindingcollectionadapter.listeners.PageListener;
import me.tatarka.bindingcollectionadapter.sample.R;
import me.tatarka.bindingcollectionadapter.sample.databinding.FragmentChildBinding;
import me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.viewmodel.ItemViewModel;
import me.tatarka.bindingcollectionadapter.sample.fragmentitem.childfragment.viewmodel.ViewModel;

/**
 * Created by wanghaiming on 2016/9/22.
 */
public class ChildFragment extends Fragment {

    private ViewModel mViewModel;
    private FragmentChildBinding mBinding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        //prepare view model
        mViewModel = new ViewModel();
        ItemListener itemListener = new ItemListener(new ItemClickListener(),new ItemLongClickListener());
        mViewModel.setPageListener(new PageListener(itemListener,new PageClickListener()));
        for(int i = 0 ; i < 10;i++){
            ItemViewModel itemViewModel = new ItemViewModel("book "+i,0,i%2==0,false);
            mViewModel.addItem(itemViewModel);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChildBinding.inflate(inflater,container,false);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }


    private class PageClickListener implements View.OnClickListener {

        private void onRemove(View v) {
            mViewModel.removeItem();
        }

        private void onAdd(View v) {
            int itemCount = mViewModel.getItemCount();
            mViewModel.addItem(new ItemViewModel("book"+itemCount,0,itemCount%2==0,false));
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.add :
                    onAdd(v);
                    break;
                case R.id.remove:
                    onRemove(v);
                    break;
                default:
            }

        }
    }
    private class ItemClickListener implements View.OnClickListener {

        private void onAdd(View v) {
            ItemViewModel itemViewModel = (ItemViewModel)v.getTag();
            itemViewModel.setBookCount(itemViewModel.getBookCount().get()+1);
        }

        private void onReduce(View v) {
            ItemViewModel itemViewModel = (ItemViewModel)v.getTag();
            int count = itemViewModel.getBookCount().get();
            if(count > 0){
                itemViewModel.setBookCount(count -1);
            }
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ibt_add :
                    onAdd(v);
                    break;
                case R.id.ibt_reduce:
                    onReduce(v);
                    break;
                case R.id.item :
                    Toast.makeText(getContext(),"item clicked.",Toast.LENGTH_SHORT).show();
                    break;
                default :
            }

        }
    }

    private class ItemLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            ItemViewModel itemViewModel = (ItemViewModel)v.getTag();
            itemViewModel.setChecked(!itemViewModel.getChecked().get());
            return true;
        }
    }
}
