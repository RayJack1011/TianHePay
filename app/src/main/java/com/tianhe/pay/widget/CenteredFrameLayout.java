package com.tianhe.pay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tianhe.pay.R;

public class CenteredFrameLayout extends FrameLayout {
    public CenteredFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    static int calculateChildLeft(int parentLeft, int parentRight, int childWidth, int leftOffset) {
        return Math.min(parentRight - childWidth, Math.max(parentLeft, parentLeft + leftOffset - childWidth / 2));
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.leftOffset >= 0) {
                int childWidth = child.getMeasuredWidth();
                int childLeft = calculateChildLeft(left, right, childWidth, layoutParams.leftOffset);
                child.layout(childLeft, child.getTop(), childLeft + childWidth, child.getBottom());
            }
        }
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public final int leftOffset;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                    .CenteredFrameLayout_LayoutParams);
            this.leftOffset = a.getDimensionPixelSize(R.styleable.CenteredFrameLayout_LayoutParams_centerOffsetLeft, -1);
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.leftOffset = -1;
        }
    }
}
