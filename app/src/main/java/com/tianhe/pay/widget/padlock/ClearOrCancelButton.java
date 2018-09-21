package com.tianhe.pay.widget.padlock;

import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;

import com.tianhe.pay.R;

class ClearOrCancelButton extends ButtonInfo {
    private Padlock padlock;

    ClearOrCancelButton(Padlock padlock, StateListDrawable var2, TextPaint var3, TextPaint var4, Padlock.ContentType var5, Key var6) {
        super(padlock, var2, var3, var4, (String) null, var5, var6);
        this.padlock = padlock;
    }

    private boolean isClear() {
        return padlock.pinPadLeftButtonState == Padlock.PinPadLeftButtonState.CLEAR || padlock
                .pinPadLeftButtonState == Padlock.PinPadLeftButtonState.CLEAR_DISABLED;
    }

    protected void click(float var1, float var2) {
        if (padlock.onKeyPressListener != null) {
            if (padlock.pinPadLeftButtonState != Padlock.PinPadLeftButtonState.CLEAR) {
                padlock.onKeyPressListener.onCancelClicked();
                return;
            }

            padlock.onKeyPressListener.onClearClicked();
        }

    }

    protected String getContent() {
        return padlock.pinPadLeftButtonState != Padlock.PinPadLeftButtonState.CANCEL && padlock
                .pinPadLeftButtonState != Padlock.PinPadLeftButtonState.CANCEL_DISABLED ? padlock.getContext
                ().getString(R.string.keypad_panel_text_clear) : padlock.getContext().getString(R.string.keypad_panel_text_cancel);
    }

    public String getDescription() {
        Resources var2 = padlock.getResources();
        int var1;
        if (this.isClear()) {
            var1 = R.string.keypad_panel_button_clear;
        } else {
            var1 = R.string.keypad_panel_button_cancel;
        }

        return var2.getString(var1);
    }

    public String getDisplayValue() {
        Resources var2 = padlock.getResources();
        int var1;
        if (this.isClear()) {
            var1 = R.string.keypad_panel_text_clear;
        } else {
            var1 = R.string.keypad_panel_text_cancel;
        }

        return var2.getString(var1);
    }

    public boolean onLongClick(float var1, float var2) {
        if (padlock.onKeyPressListener != null && padlock.pinPadLeftButtonState == Padlock
                .PinPadLeftButtonState.CLEAR) {
            padlock.onKeyPressListener.onClearLongPressed();
        }

        return true;
    }

    public void setEnabled(boolean var1) {
        Padlock.PinPadLeftButtonState var2;
        switch (padlock.pinPadLeftButtonState) {
            case CLEAR:
            case CLEAR_DISABLED:
                if (var1) {
                    var2 = Padlock.PinPadLeftButtonState.CLEAR;
                } else {
                    var2 = Padlock.PinPadLeftButtonState.CLEAR_DISABLED;
                }
                break;
            default:
                if (var1) {
                    var2 = Padlock.PinPadLeftButtonState.CANCEL;
                } else {
                    var2 = Padlock.PinPadLeftButtonState.CANCEL_DISABLED;
                }
        }

        padlock.pinPadLeftButtonState = var2;
        super.setEnabled(var1);
    }

    public void updateState() {
        boolean var1;
        if (padlock.pinPadLeftButtonState != Padlock.PinPadLeftButtonState.CLEAR && padlock
                .pinPadLeftButtonState != Padlock.PinPadLeftButtonState.CANCEL) {
            var1 = false;
        } else {
            var1 = true;
        }

        this.setEnabled(var1);
        padlock.invalidate();
    }
}
