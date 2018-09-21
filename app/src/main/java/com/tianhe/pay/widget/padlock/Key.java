package com.tianhe.pay.widget.padlock;

import android.content.res.Resources;

import com.tianhe.pay.R;

public enum Key {
    BACKSPACE(R.string.keypad_panel_text_backspace, R.string.keypad_panel_button_backspace),
    CHECK_OR_SKIP,
    CLEAR_OR_CANCEL,
    DECIMAL(R.string.keypad_panel_text_decimal, R.string.keypad_panel_button_decimal),
    DOUBLE_ZERO(R.string.keypad_panel_text_00, R.string.keypad_panel_button_00),
    EIGHT(R.string.keypad_panel_text_8, R.string.keypad_panel_button_8),
    FIVE(R.string.keypad_panel_text_5, R.string.keypad_panel_button_5),
    FOUR(R.string.keypad_panel_text_4, R.string.keypad_panel_button_4),
    NINE(R.string.keypad_panel_text_9, R.string.keypad_panel_button_9),
    ONE(R.string.keypad_panel_text_1, R.string.keypad_panel_button_1),
    PLUS(R.string.keypad_panel_text_plus, R.string.keypad_panel_button_plus),
    ROUNDED_PLUS(R.string.keypad_panel_text_plus, R.string.keypad_panel_button_plus),
    SEVEN(R.string.keypad_panel_text_7, R.string.keypad_panel_button_7),
    SIX(R.string.keypad_panel_text_6, R.string.keypad_panel_button_6),
    SUBMIT(R.string.keypad_panel_text_submit, R.string.keypad_panel_button_submit),
    THREE(R.string.keypad_panel_text_3, R.string.keypad_panel_button_3),
    TWO(R.string.keypad_panel_text_2, R.string.keypad_panel_button_2),
    ZERO(R.string.keypad_panel_text_0, R.string.keypad_panel_button_0),
    UNKNOWN(-1, -1);

    private int descriptionId;
    private int displayValueId;

    private Key() {
        this(-1, -1);
    }

    private Key(int var3, int var4) {
        this.displayValueId = var3;
        this.descriptionId = var4;
    }

    public String getDescription(Resources var1) {
        return this.descriptionId == -1 ? null : var1.getString(this.descriptionId);
    }

    public String getDisplayValue(Resources var1) {
        return this.displayValueId == -1 ? null : var1.getString(this.displayValueId);
    }
}
