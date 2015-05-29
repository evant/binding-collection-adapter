package me.tatarka.bindingcollectionadapter.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by evantatarka on 5/26/15.
 */
public class BindingAdapters {
    @BindingAdapter("fadeVisible")
    public static void setFadeVisible(final View view, boolean visible) {
        if (view.getTag() == null) {
            view.setTag(true);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        } else {
            view.animate().cancel();

            if (visible) {
                view.setVisibility(View.VISIBLE);
                view.setAlpha(0);
                view.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(1);
                    }
                });
            } else {
                view.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(1);
                        view.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}
