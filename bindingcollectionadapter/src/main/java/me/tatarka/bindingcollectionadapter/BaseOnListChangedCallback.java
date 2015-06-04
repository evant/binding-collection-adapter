package me.tatarka.bindingcollectionadapter;

import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;

/**
 * OnListChangedCallbackHelper to ensure callbacks are run on the main therad.
 */
abstract class BaseOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    final Handler handler = new Handler(Looper.getMainLooper());
    
    void onMainThread(final OnMainThread onMainThread) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            onMainThread.onMainThread();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onMainThread.onMainThread();
                }
            });
        }
    }
    
    interface OnMainThread {
        void onMainThread();
    } 
}
