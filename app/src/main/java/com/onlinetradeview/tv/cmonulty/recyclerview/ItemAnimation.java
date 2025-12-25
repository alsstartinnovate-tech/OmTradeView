package com.onlinetradeview.tv.cmonulty.recyclerview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class ItemAnimation {

    /* animation type */
    public static final int BOTTOM_UP = 1;
    public static final int FADE_IN = 2;
    public static final int LEFT_RIGHT = 3;
    public static final int RIGHT_LEFT = 4;
    public static final int FAST_FADE = 5;
    public static final int NONE = 0;

    /* animation duration - optimized for smooth performance */
    private static final long DURATION_IN_BOTTOM_UP = 300;
    private static final long DURATION_IN_FADE_ID = 400;
    private static final long DURATION_IN_LEFT_RIGHT = 250;
    private static final long DURATION_IN_RIGHT_LEFT = 250;
    
    /* interpolators for smoother animations */
    private static final AccelerateDecelerateInterpolator ACCEL_DECEL = new AccelerateDecelerateInterpolator();
    private static final DecelerateInterpolator DECELERATE = new DecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT = new OvershootInterpolator(1.2f);

    public static void animate(View view, int position, int type) {
        switch (type) {
            case BOTTOM_UP:
                animateBottomUp(view, position);
                break;

            case FADE_IN:
                animateFadeIn(view, position);
                break;

            case LEFT_RIGHT:
                animateLeftRight(view, position);
                break;
            case RIGHT_LEFT:
                animateRightLeft(view, position);
                break;
            case FAST_FADE:
                animateNew(view,position);
                break;
        }
    }

    private static void animateBottomUp(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setTranslationY(not_first_item ? 200 : 100);
        view.setAlpha(0.f);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 
            not_first_item ? 200 : 100, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 1.f);
        
        animatorTranslateY.setInterpolator(DECELERATE);
        animatorAlpha.setInterpolator(ACCEL_DECEL);
        
        long delay = not_first_item ? 0 : Math.min(position * 50, 200);
        animatorTranslateY.setStartDelay(delay);
        animatorTranslateY.setDuration(DURATION_IN_BOTTOM_UP);
        animatorAlpha.setDuration(DURATION_IN_BOTTOM_UP);
        
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }

    private static void animateFadeIn(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setAlpha(0.f);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 1.f);
        animatorAlpha.setInterpolator(ACCEL_DECEL);
        
        long delay = not_first_item ? 0 : Math.min(position * 80, 300);
        animatorAlpha.setStartDelay(delay);
        animatorAlpha.setDuration(DURATION_IN_FADE_ID);
        
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }

    private static void animateNew(View view, int position) {
        int DURATION_IN_FAST_FADE = 250;
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setAlpha(0.f);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 1.f);
        animatorAlpha.setInterpolator(ACCEL_DECEL);
        
        long delay = not_first_item ? 0 : Math.min(position * 50, 200);
        animatorAlpha.setStartDelay(delay);
        animatorAlpha.setDuration(DURATION_IN_FAST_FADE);
        
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }

    private static void animateLeftRight(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setTranslationX(-200f);
        view.setAlpha(0.f);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(view, "translationX", -200f, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 1.f);
        
        animatorTranslateX.setInterpolator(DECELERATE);
        animatorAlpha.setInterpolator(ACCEL_DECEL);
        
        long delay = not_first_item ? 0 : Math.min(position * 50, 200);
        animatorTranslateX.setStartDelay(delay);
        animatorTranslateX.setDuration(DURATION_IN_LEFT_RIGHT);
        animatorAlpha.setDuration(DURATION_IN_LEFT_RIGHT);
        
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    private static void animateRightLeft(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        float startX = view.getX() + 200;
        view.setTranslationX(startX);
        view.setAlpha(0.f);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(view, "translationX", startX, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 1.f);
        
        animatorTranslateX.setInterpolator(DECELERATE);
        animatorAlpha.setInterpolator(ACCEL_DECEL);
        
        long delay = not_first_item ? 0 : Math.min(position * 50, 200);
        animatorTranslateX.setStartDelay(delay);
        animatorTranslateX.setDuration(DURATION_IN_RIGHT_LEFT);
        animatorAlpha.setDuration(DURATION_IN_RIGHT_LEFT);
        
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

}
