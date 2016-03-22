package me.tatarka.bindingcollectionadapter.bench;

import java.util.Random;

import io.leocad.delta.BenchmarkTask;

public class ItemView3Bench extends BenchmarkTask {
    Random random;
    int bindingVar;
    int layoutRes;

    @Override
    protected void onPreExecute() {
        random = new Random();
    }

    @Override
    protected Object task() {
        long itemView = pack(random.nextInt(), random.nextInt());
        bindingVar = bindingVar(bindingVar);
        layoutRes = layoutRes(layoutRes);
        return itemView;
    }

    static long pack(int bindingVar, int layoutRes) {
        return (bindingVar << 16) | layoutRes;
    }

    static int bindingVar(long itemView) {
        return (int) (itemView >> 16);
    }

    static int layoutRes(long itemView) {
        return (int) (itemView & 0xffffffffL);
    }

}
