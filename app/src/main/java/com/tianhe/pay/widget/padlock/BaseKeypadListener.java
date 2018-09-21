package com.tianhe.pay.widget.padlock;

public abstract class BaseKeypadListener implements Padlock.OnKeyPressListener {
    private final Padlock keypad;

    public BaseKeypadListener(Padlock keypad) {
        this.keypad = keypad;
    }

    protected abstract boolean backspaceEnabled();

    protected abstract boolean digitsEnabled();

    protected abstract boolean submitEnabled();

    public void updateBackspaceState() {
        this.keypad.setBackspaceEnabled(backspaceEnabled());
    }

    public void updateDigitsState() {
        this.keypad.setDigitsEnabled(digitsEnabled());
    }

    public void updateSubmitState() {
        this.keypad.setSubmitEnabled(submitEnabled());
    }
}
