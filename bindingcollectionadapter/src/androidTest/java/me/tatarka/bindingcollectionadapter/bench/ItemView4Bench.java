package me.tatarka.bindingcollectionadapter.bench;

import android.support.v4.util.Pools;

import java.util.Random;

import io.leocad.delta.BenchmarkTask;
import me.tatarka.bindingcollectionadapter.ItemView;

public class ItemView4Bench extends BenchmarkTask {

    static final Pools.Pool<ItemView> ITEM_VIEW_POOL = new Pools.SimplePool<>(1000);

    Random random;
    int bindingVar;
    int layoutRes;

    @Override
    protected void onPreExecute() {
        random = new Random();
    }

    @Override
    protected Object task() {
        ItemView itemView = pool(random.nextInt(), random.nextInt());
        bindingVar = itemView.bindingVariable();
        layoutRes = itemView.layoutRes();
        release(itemView);
        return itemView;
    }


    static ItemView pool(int bindingVar, int layoutRes) {
        ItemView itemView = ITEM_VIEW_POOL.acquire();
        if (itemView == null) {
            itemView = new ItemView();
        }
        itemView.set(bindingVar, layoutRes);
        return itemView;
    }

    static void release(ItemView itemView) {
        ITEM_VIEW_POOL.release(itemView);
    }
}
