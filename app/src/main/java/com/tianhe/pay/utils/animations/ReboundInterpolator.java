package com.tianhe.pay.utils.animations;

import android.view.animation.Interpolator;

public class ReboundInterpolator implements Interpolator {
    public ReboundInterpolator() {
    }

    private static float bounce(float t) {
        return t * t * 8.0F;
    }

    public float getInterpolation(float t) {
        t *= 1.1226F;
        return t < 0.3535F ? bounce(t) :
                (t < 0.7408F ? (-bounce(t - 0.54719F) + 1.3F) :
                        (t < 0.9644F ? (bounce(t - 0.8526F) + 0.9F) : -bounce(t - 1.0435F) + 1.05F));
    }

}