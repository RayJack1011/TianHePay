package com.tianhe.pay.widget.padlock;

import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;

import com.tianhe.pay.R;

class ClearButtonInfo extends ButtonInfo {
    private Padlock padlock;

    ClearButtonInfo(Padlock padlock, StateListDrawable var2, TextPaint var3, TextPaint var4, String var5, Padlock.ContentType
            var6, Key var7) {
        super(padlock, var2, var3, var4, var5, var6, var7);
        this.padlock = padlock;
    }

    protected void click(float var1, float var2) {
        if (padlock.onKeyPressListener != null) {
            padlock.onKeyPressListener.onClearClicked();
        }

    }

    public String getDescription() {
        return padlock.getResources().getString(R.string.keypad_panel_button_clear);
    }

    public String getDisplayValue() {
        return padlock.getResources().getString(R.string.keypad_panel_text_clear);
    }

    protected boolean longClick(float var1, float var2) {
        if (padlock.onKeyPressListener != null) {
            padlock.onKeyPressListener.onClearLongPressed();
        }

        return true;
    }
}
