package com.tianhe.pay.widget.padlock;

import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;

public abstract class ButtonInfo {
    private Padlock padlock;
    protected final StateListDrawable background;
    private final String content;
    protected final TextPaint contentDisabledPaint;
    protected final TextPaint contentPaint;
    private final Padlock.ContentType contentType;
    private String description;
    private String displayValue;
    private boolean enabled;
    private final String letters;
    protected final TextPaint lettersDisabledPaint;
    protected final TextPaint lettersPaint;
    private boolean longPressed;
    private boolean pressed;
    private float left;
    private float bottom;
    private float right;
    private float top;

    public ButtonInfo(Padlock padlock, StateListDrawable background,
                      TextPaint contentPaint, TextPaint contentDisabledPaint, String content,
                      TextPaint lettersPaint,
                      TextPaint lettersDisabledPaint, String letters, Padlock.ContentType contentType, Key key) {
        this.padlock = padlock;
        this.enabled = true;
        this.background = background;
        this.content = content;
        this.letters = letters;
        this.contentPaint = contentPaint;
        this.contentDisabledPaint = contentDisabledPaint;
        this.lettersPaint = lettersPaint;
        this.lettersDisabledPaint = lettersDisabledPaint;
        this.contentType = contentType;
        this.displayValue = key.getDisplayValue(padlock.getResources());
        this.description = key.getDescription(padlock.getResources());
    }

    public ButtonInfo(Padlock padlock, StateListDrawable background,
                      TextPaint contentPaint, TextPaint contentDisabledPaint, String content,
                      Padlock.ContentType contentType, Key key) {
        this(padlock, background, contentPaint, contentDisabledPaint, content, null, null, null, contentType, key);
    }

    public float centerX() {
        return (this.getLeft() + this.getRight()) / 2.0F;
    }

    public float centerY() {
        return (this.getTop() + this.getBottom()) / 2.0F;
    }

    public void clearPress() {
        this.pressed = false;
        this.longPressed = false;
    }

    protected abstract void click(float x, float y);

    public boolean contains(float x, float y) {
        return this.enabled
                && x >= this.getLeft() && x <= this.getRight()
                && y >= this.getTop() && y <= this.getBottom();
    }

    protected float drawContentY() {
        float centerY = this.centerY();
        float baseLineY = centerY - (contentPaint.descent() + contentPaint.ascent() / 2.0F);
        if (this.contentType == Padlock.ContentType.TEXT) {
            baseLineY += (float) padlock.getTypefaceSize() * 0.06125F;
        } else {
            if (this.contentType == Padlock.ContentType.MARIN_GLYPH) {
                baseLineY += contentPaint.getTextSize() / 4.25F;
            }
        }
        if (this.letters != null) {
            baseLineY -= ((float) padlock.getLettersTypefaceSize() + padlock.getLineSpaceExtra()) / 2.0F;
        }
        return baseLineY;
    }

    protected float drawLettersY() {
        float centerY = this.centerY();
        float baseLineY = centerY - (lettersPaint.descent() + lettersPaint.ascent() / 2.0F)
                + (float) padlock.getLettersTypefaceSize() * 0.06125F;
        if (this.getContent() != null) {
            baseLineY += ((float) padlock.getLettersTypefaceSize() + padlock.getLineSpaceExtra()) / 2.0F;
        }
        return baseLineY;
    }

    public void draw(Canvas canvas) {
        this.drawBackground(canvas);
        if (this.getContent() != null) {
            TextPaint contentPaint = this.getContentPaint();
            canvas.drawText(this.getContent(), this.centerX(), drawContentY(), contentPaint);
        }

        if (this.letters != null) {
            TextPaint lettersPaint = this.getLettersPaint();
            canvas.drawText(this.letters, this.centerX(), drawLettersY(), lettersPaint);
        }
    }

    protected void drawBackground(Canvas var1) {
        this.background.setState(this.getDrawableState());
        this.background.draw(var1);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ButtonInfo other = (ButtonInfo) obj;
        if ((Float.compare(other.getLeft(), this.getLeft()) == 0)
                && (Float.compare(other.getTop(), this.getTop()) == 0)
                && (Float.compare(other.getRight(), this.getRight()) == 0)
                && (Float.compare(other.getBottom(), this.getBottom()) == 0)) {
            return true;
        }
        return false;
    }

    protected String getContent() {
        return this.content;
    }

    protected TextPaint getContentPaint() {
        return this.enabled ? this.contentPaint : this.contentDisabledPaint;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }

    protected int[] getDrawableState() {
        return !this.enabled ? new int[0] : (this.pressed && !padlock.isPinMode() ? new int[]{android.R.attr
                .state_pressed} : new
                int[]{android.R.attr.state_enabled});
    }

    protected TextPaint getLettersPaint() {
        return this.enabled ? this.lettersPaint : this.lettersDisabledPaint;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    protected boolean longClick(float var1, float var2) {
        return false;
    }

    public void onClick(float var1, float var2) {
        if (this.pressed && !this.longPressed && this.contains(var1, var2)) {
            if (!padlock.isPinMode()) {
                padlock.performHapticFeedback(1);
                AccessibilityUtils.makeAnnouncement(padlock, this.getDisplayValue());
            }

            this.click(var1, var2);
        }

        this.clearPress();
    }

    public boolean onLongClick(float var1, float var2) {
        if (this.pressed && this.contains(var1, var2) && this.longClick(var1, var2)) {
            padlock.performHapticFeedback(0);
            this.longPressed = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean onPressed(float var1, float var2) {
        if (this.contains(var1, var2)) {
            this.pressed = true;
            this.longPressed = false;
            return true;
        } else {
            return false;
        }
    }

    int round(double var1) {
        return (int) (0.5D + var1);
    }

    public void setEnabled(boolean var1) {
        this.enabled = var1;
    }

    public void setLocation(float var1, float var2, float var3, float var4) {
        this.background.setBounds(this.round((double) var1), this.round((double) var2), this.round((double) var3)
                , this.round((double) var4));
        this.left = var1;
        this.top = var2;
        this.right = var3;
        this.bottom = var4;
    }

    public float getLeft() {
        return left;
    }

    public float getBottom() {
        return bottom;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }
}
