package com.tianhe.pay.ui;

import android.view.animation.Interpolator;

public class Interpolators {
    public static final Interpolator CubicEaseIn = new Interpolator() {
        public float getInterpolation(float t) {
            return t * t * t;
        }
    };
    public static final Interpolator CubicEaseInEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            t /= 0.5F;
            if(t < 1.0F) {
                return 0.5F * t * t * t;
            } else {
                t -= 2.0F;
                return (t * t * t + 2.0F) * 0.5F;
            }
        }
    };
    public static final Interpolator CubicEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            return (t - 1.0F) * t * t + 1.0F;
        }
    };
    public static final Interpolator QintEaseIn = new Interpolator() {
        public float getInterpolation(float t) {
            return t * t * t * t * t;
        }
    };
    public static final Interpolator QintEaseInEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            t /= 0.5F;
            if(t < 1.0F) {
                return 0.5F * t * t * t * t * t;
            } else {
                t -= 2.0F;
                return (t * t * t * t * t + 2.0F) * 0.5F;
            }
        }
    };
    public static final Interpolator QintEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            return (t - 1.0F) * t * t * t * t + 1.0F;
        }
    };
    public static final Interpolator QuadEaseIn = new Interpolator() {
        public float getInterpolation(float t) {
            return t * t;
        }
    };
    public static final Interpolator QuadEaseInEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            t /= 0.5F;
            if(t < 1.0F) {
                return 0.5F * t * t;
            } else {
                --t;
                return -0.5F * ((t - 2.0F) * t - 1.0F);
            }
        }
    };
    public static final Interpolator QuadEaseOut = new Interpolator() {
        public float getInterpolation(float t) {
            return -t * (t - 2.0F);
        }
    };
}
