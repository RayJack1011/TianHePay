package com.tianhe.pay.utils.animations;

import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

public class ShakeAnimation extends TranslateAnimation {

    public ShakeAnimation() {
        this(-45.0F);
    }

    public ShakeAnimation(float deltaX) {
        super(deltaX, 0.0F, 0.0F, 0.0F);
        this.setDuration(400L);
        this.setInterpolator(new BounceInterpolator());
    }
}
