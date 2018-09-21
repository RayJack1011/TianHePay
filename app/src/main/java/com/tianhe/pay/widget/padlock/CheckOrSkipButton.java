package com.tianhe.pay.widget.padlock;

import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;

import com.tianhe.pay.R;

import java.util.ArrayList;

class CheckOrSkipButton extends ButtonInfo {
    private Padlock padlock;
    final TextPaint checkPaint;

    CheckOrSkipButton(Padlock padlock, StateListDrawable var2, TextPaint var3, TextPaint var4, TextPaint var5, Padlock.ContentType
            var6, Key var7) {
        super(padlock, var2, var3, var4, (String) null, var6, var7);
        this.padlock = padlock;
        this.checkPaint = var5;
    }

    private boolean isCheck() {
        return padlock.pinPadRightButtonState == Padlock.PinPadRightButtonState.CHECK_VALID || padlock
                .pinPadRightButtonState == Padlock.PinPadRightButtonState.CHECK_INVALID;
    }

    protected void click(float var1, float var2) {
        if (padlock.onKeyPressListener != null) {
            if (padlock.pinPadRightButtonState == Padlock.PinPadRightButtonState.SKIP_ENABLED) {
                padlock.onKeyPressListener.onSkipClicked();
            } else if (padlock.pinPadRightButtonState == Padlock.PinPadRightButtonState.CHECK_VALID) {
                padlock.onKeyPressListener.onSubmitClicked();
                return;
            }
        }

    }

    protected String getContent() {
        return padlock.pinPadRightButtonState != Padlock.PinPadRightButtonState.SKIP_ENABLED && padlock
                .pinPadRightButtonState != Padlock.PinPadRightButtonState.SKIP_DISABLED ? Glyph.CHECK.getLetter()
                : padlock.getResources().getString(R.string.keypad_panel_text_backspace);
    }

    protected TextPaint getContentPaint() {
        Padlock.PinPadRightButtonState var1 = padlock.pinPadRightButtonState;
        switch (var1) {
            case SKIP_DISABLED:
                return contentDisabledPaint;
            case SKIP_ENABLED:
                return contentPaint;
            default:
                return checkPaint;
        }
    }

    public String getDescription() {
        Resources var2 = padlock.getResources();
        int var1;
        if (this.isCheck()) {
            var1 = R.string.keypad_panel_button_submit;
        } else {
            var1 = R.string.keypad_panel_button_submit;
        }
        return var2.getString(var1);
    }

    public String getDisplayValue() {
        Resources var2 = padlock.getResources();
        int var1;
        if (this.isCheck()) {
            var1 = R.string.keypad_panel_text_submit;
        } else {
            var1 = R.string.keypad_panel_text_skip;
        }

        return var2.getString(var1);
    }

    protected int[] getDrawableState() {
        ArrayList<Integer> states = new ArrayList<>();
        if (padlock.isPressed()) {
            states.add(android.R.attr.state_pressed);
        }
        switch (padlock.pinPadRightButtonState) {
            case CHECK_INVALID:
                states.add(android.R.attr.state_enabled);
                states.add(android.R.attr.state_active);
                break;
            case CHECK_VALID:
                states.add(android.R.attr.state_active);

        }

        int[] result = new int[states.size()];
        for (int i = 0; i < states.size(); ++i) {
            result[i] = states.get(i);
        }
        return result;
    }

    public void setEnabled(boolean enabled) {
        Padlock.PinPadRightButtonState rightButtonState;
        switch (padlock.pinPadRightButtonState) {
            case CHECK_INVALID:
            case CHECK_VALID:
                if (enabled) {
                    rightButtonState = Padlock.PinPadRightButtonState.CHECK_VALID;
                } else {
                    rightButtonState = Padlock.PinPadRightButtonState.CHECK_INVALID;
                }
                break;
            case SKIP_ENABLED:
            case SKIP_DISABLED:
                if (enabled) {
                    rightButtonState = Padlock.PinPadRightButtonState.SKIP_ENABLED;
                } else {
                    rightButtonState = Padlock.PinPadRightButtonState.SKIP_DISABLED;
                }
                break;
            default:
                throw new IllegalArgumentException("No such right button state " + padlock
                        .pinPadRightButtonState);
        }

        padlock.pinPadRightButtonState = rightButtonState;
        super.setEnabled(enabled);
    }

    public void updateState() {
        boolean enable;
        if (padlock.pinPadRightButtonState != Padlock.PinPadRightButtonState.SKIP_ENABLED && padlock
                .pinPadRightButtonState != Padlock.PinPadRightButtonState.CHECK_VALID) {
            enable = false;
        } else {
            enable = true;
        }

        super.setEnabled(enable);
        padlock.invalidate();
    }
}
