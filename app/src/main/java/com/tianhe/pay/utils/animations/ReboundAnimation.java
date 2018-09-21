package com.tianhe.pay.utils.animations;

import android.view.animation.TranslateAnimation;

public class ReboundAnimation extends TranslateAnimation {
    public ReboundAnimation(float deltaX) {
        super(deltaX, 0.0F, 0.0F, 0.0F);
        this.setDuration(200L);
        this.setInterpolator(new ReboundInterpolator());
    }
}
