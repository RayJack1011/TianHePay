package com.tianhe.pay.utils.animations;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;

public class Animations {
    public static final int PULSE_DURATION = 333;

    public static Animation buildPulseAnimation() {
        ScaleAnimation animation = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1, 0.5F, 1, 0.5F);
        animation.setInterpolator(new Interpolator() {
            private Interpolator easeInEaseOut = new AccelerateDecelerateInterpolator();

            public float getInterpolation(float input) {
                input = this.easeInEaseOut.getInterpolation(input);
                if (input < 0.34D) {
                    return 1.0F + 0.25F * (input / 0.33F);
                }
                if (input < 0.67D) {
                    return 1.25F - 0.32F * ((input - 0.33F) / 0.33F);
                }
                return 0.93F + 0.06999999F * ((input - 0.66F) / 0.33F);
            }
        });
        animation.setDuration(PULSE_DURATION);
        return animation;
    }
}
