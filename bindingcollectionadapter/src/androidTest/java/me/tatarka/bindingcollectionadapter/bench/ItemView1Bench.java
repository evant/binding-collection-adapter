package me.tatarka.bindingcollectionadapter.bench;

import java.util.Random;

import io.leocad.delta.BenchmarkTask;
import me.tatarka.bindingcollectionadapter.ItemView;

public class ItemView1Bench extends BenchmarkTask {

    Random random;
    ItemView itemView;
    int bindingVar;
    int layoutRes;

    @Override
    public void onPreExecute() {
        random = new Random();
        itemView = new ItemView();
    }

    @Override
    public Object task() {
        itemView.set(random.nextInt(), random.nextInt());
        bindingVar = itemView.bindingVariable();
        layoutRes = itemView.layoutRes();
        return itemView;
    }
}
