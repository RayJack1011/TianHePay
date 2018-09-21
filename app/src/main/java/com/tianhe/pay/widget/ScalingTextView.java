package com.tianhe.pay.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

public class ScalingTextView extends AppCompatTextView {
    private int lastMeasuredWidth;
    private float maxTextSize;
    private float minTextSize;

    public ScalingTextView(Context context) {
        super(context);
    }

    public ScalingTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
    }

    public ScalingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        if (width != this.lastMeasuredWidth) {
            int availableWidth = this.getAvailableWidth(width);
            int fittedTextSize = getFittedTextSize(this.getPaint(), this.getText(), availableWidth, this.minTextSize, this.maxTextSize);
            super.setTextSize(0, fittedTextSize);
        }
        super.onMeasure(widthSpec, heightSpec);
        this.lastMeasuredWidth = this.getMeasuredWidth();
    }

    private int getAvailableWidth(int availableWidth) {
        Drawable[] compoundDrawables = this.getCompoundDrawables();
        int result = availableWidth;
        if (compoundDrawables[0] != null) {
            result = availableWidth - this.getCompoundPaddingLeft() - compoundDrawables[0].getIntrinsicWidth();
        }
        if (compoundDrawables[2] != null) {
            result = result - this.getCompoundPaddingRight() - compoundDrawables[2].getIntrinsicWidth();
        }

        return result - (this.getPaddingLeft() + this.getPaddingRight());
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.minTextSize = ss.minSize;
        this.maxTextSize = ss.maxSize;
    }

    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.minTextSize, this.maxTextSize);
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.requestLayout();
    }

    public void setGravity(int gravity) {
        super.setGravity(gravity);
        this.requestLayout();
    }

    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        this.maxTextSize = this.getTextSize();
        this.requestLayout();
    }

    public void setTextSize(float size) {
        super.setTextSize(size);
        this.maxTextSize = this.getTextSize();
        this.lastMeasuredWidth = 0;
        this.requestLayout();
    }

    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        this.maxTextSize = this.getTextSize();
        this.lastMeasuredWidth = 0;
        this.requestLayout();
    }

    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
        this.requestLayout();
    }

    public void setMinTextSize(float minTextSize) {
        this.minTextSize = minTextSize;
    }

    private static int getFittedTextSize(TextPaint paint, CharSequence text,
                                         int targetWidth, float minTextSize, float maxTextSize) {
        if (targetWidth <= 0) {
            return (int) minTextSize;
        } else {
            TextPaint textPaint = new TextPaint(paint);
            textPaint.setTextScaleX(1.0F);
            textPaint.setTextSize(100.0F);
            float estimate = textPaint.measureText(text, 0, text.length());
            return (int) Math.floor((double) Math.min(Math.max((float) targetWidth / estimate * 100.0F, minTextSize),
                    maxTextSize));
        }
    }

    private static class SavedState extends BaseSavedState implements Parcelable {
        private final float maxSize;
        private final float minSize;

        protected SavedState(Parcelable superState, float maxSize, float minSize) {
            super(superState);
            this.maxSize = maxSize;
            this.minSize = minSize;
        }

        protected SavedState(Parcel in) {
            super(in);
            this.maxSize = in.readFloat();
            this.minSize = in.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(this.maxSize);
            dest.writeFloat(this.minSize);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
