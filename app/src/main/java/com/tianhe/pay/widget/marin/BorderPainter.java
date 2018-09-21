package com.tianhe.pay.widget.marin;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BorderPainter {

    @Retention(RetentionPolicy.SOURCE)
    public @interface Borders {
    }
    public static final int ALL = 15;
    public static final int NONE = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 4;
    public static final int BOTTOM = 8;
    private final Paint borderPaint;
    private int borderWidth;
    private final Rect canvasBounds;
    private boolean drawInside;
    private final View hostView;
    private int leftInset;
    private int rightInset;
    private int sidesToPaint;

    public BorderPainter(View hostView, @DimenRes int borderWidth, @ColorRes int borderColor) {
        this.canvasBounds = new Rect();
        this.sidesToPaint = 0;
        this.hostView = hostView;
        Resources res = hostView.getResources();
        this.borderPaint = new Paint();
        this.borderPaint.setColor(res.getColor(borderColor));
        this.borderWidth = res.getDimensionPixelSize(borderWidth);
        this.drawInside = true;
        this.rightInset = 0;
        this.leftInset = 0;
    }

    public void addBorder(int side) {
        this.sidesToPaint |= side;
    }

    public void clearBorders() {
        this.sidesToPaint = NONE;
    }

    public void clearEmphasis() {
        this.borderPaint.setXfermode(null);
    }

    public void drawBorderInside() {
        this.drawInside = true;
    }

    public void drawBorderOutside() {
        this.drawInside = false;
    }

    public void drawBorders(Canvas canvas) {
        if(!this.drawInside) {
            throw new IllegalStateException("Can only drawBorders on the inside!");
        } else {
            if(this.hostView.getVisibility() == View.VISIBLE) {
                canvas.getClipBounds(this.canvasBounds);
                if((this.sidesToPaint & LEFT) != 0) {
                    canvas.drawRect(canvasBounds.left, canvasBounds.top,
                            (canvasBounds.left + borderWidth),
                            canvasBounds.bottom, borderPaint);
                }
                if((this.sidesToPaint & TOP) != 0) {
                    canvas.drawRect((canvasBounds.left + leftInset),
                            canvasBounds.top, (canvasBounds.right - rightInset),
                            (canvasBounds.top + borderWidth), borderPaint);
                }
                if((this.sidesToPaint & RIGHT) != 0) {
                    canvas.drawRect((canvasBounds.right - borderWidth), canvasBounds.top,
                            canvasBounds.right, canvasBounds.bottom, borderPaint);
                }
                if((this.sidesToPaint & BOTTOM) != 0) {
                    canvas.drawRect((canvasBounds.left + leftInset),
                            (canvasBounds.bottom - borderWidth),
                            (canvasBounds.right - rightInset), canvasBounds.bottom, borderPaint);
                }
            }

        }
    }
}
