package me.tatarka.bindingcollectionadapter.bench;

import java.util.Random;

import io.leocad.delta.BenchmarkTask;
import me.tatarka.bindingcollectionadapter.ItemView;

public class ItemView2Bench extends BenchmarkTask {
    Random random;
    int bindingVar;
    int layoutRes;

    @Override
    protected void onPreExecute() {
        random = new Random();
    }

    @Override
    protected Object task() {
        ItemView itemView = ItemView.of(random.nextInt(), random.nextInt());
        bindingVar = itemView.bindingVariable();
        layoutRes = itemView.layoutRes();
        return itemView;
    }
}
