package me.tatarka.bindingcollectionadapter;

import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * OnListChangedCallbackHelper to ensure callbacks are run on the main therad.
 */
abstract class BaseOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private static final String TAG = "BCAdapters";
    final Handler handler = new Handler(Looper.getMainLooper());

    void onMainThread(final OnMainThread onMainThread) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            onMainThread.onMainThread();
        } else {
            Log.w(TAG, "Don't change the list off the main thread! Background-thread changes don't carry their weight and will be removed from a future version.");
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
