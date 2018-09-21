package com.tianhe.pay.ui.welcome;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.tianhe.pay.utils.MutableBoolean;
import com.tianhe.pay.utils.animations.EmptyAnimationListener;

import io.reactivex.Observable;

class WelcomeAnimationControl {
    private View view;
    Animation animation;

    BehaviorRelay<Boolean> finished;
//    MutableBoolean finished;

    public WelcomeAnimationControl(View view) {
        this.view = view;
        animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(3000);
        finished = BehaviorRelay.createDefault(false);
        animation.setAnimationListener(new EmptyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                finished.accept(true);
            }
        });
    }

    Observable<Boolean> animationFinish() {
        return finished.distinctUntilChanged();
    }

    void cancelAnimation() {
        animation.cancel();
    }

    void startAnimation() {
        view.startAnimation(animation);
    }
}
