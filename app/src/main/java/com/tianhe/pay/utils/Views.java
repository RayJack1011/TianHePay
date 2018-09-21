package com.tianhe.pay.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;
import android.view.View;

public class Views {

    public static float dipsToPixels(float dips, View view) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dips, view.getResources().getDisplayMetrics());
    }

    public static NinePatchDrawable get9PatchSized(Resources res, int ninePatchId, int dimen) {
        NinePatchDrawable ninePatch = (NinePatchDrawable) res.getDrawable(ninePatchId);
        Rect npdBounds = new Rect(0, 0, dimen, dimen);
        ninePatch.setBounds(npdBounds);
        return ninePatch;
    }

    public static BitmapDrawable tryCopyToBitmapDrawable(View view, boolean resetViewPressedState) {
        Bitmap bitmapCopy = tryCopyToBitmap(view, resetViewPressedState);
        return new BitmapDrawable(view.getResources(), bitmapCopy);
    }

    public static Bitmap tryCopyToBitmap(View view) {
        return tryCopyToBitmap(view, false);
    }

    public static Bitmap tryCopyToBitmap(View view, boolean resetViewPressedState) {
        if (resetViewPressedState) {
            view.setPressed(false);
        }
        boolean drawingCacheBuilt = false;
        try {
            view.buildDrawingCache();
            drawingCacheBuilt = true;
            return view.getDrawingCache();
        } catch (OutOfMemoryError error) {
        } catch (Throwable t) {
        } finally {
            if (!drawingCacheBuilt) {
                view.destroyDrawingCache();
            }
        }
        return null;
    }

    public static void endOnDetach(final Animator animator, final View view) {
        if (animator == null || view == null) {
            return;
        }
        final View.OnAttachStateChangeListener attachListener = new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                v.removeOnAttachStateChangeListener(this);
                animator.end();
            }
        };
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.removeOnAttachStateChangeListener(attachListener);
            }
        });
    }
}
