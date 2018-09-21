package com.tianhe.pay.ui;

import com.tianhe.pay.widget.padlock.BaseKeypadListener;
import com.tianhe.pay.widget.padlock.Padlock;

public abstract class MoneyKeypadListener extends BaseKeypadListener {
    public static final long DEFAULT_MAX = 9999999;
    private final long maxAmount;

    public MoneyKeypadListener(Padlock keypad) {
        super(keypad);
        this.maxAmount = DEFAULT_MAX;
    }

    public MoneyKeypadListener(Padlock keypad, long maxAmount) {
        super(keypad);
        this.maxAmount = maxAmount;
    }

    public abstract void updateAmount(long newAmount);

    protected boolean backspaceEnabled() {
        return getAmount() > 0L;
    }

    protected boolean digitsEnabled() {
        return true;
    }

    public abstract long getAmount();

    public void onBackspaceClicked() {
        long currentAmount = getAmount();
        if (currentAmount < 10L) {
            this.updateAmount(0L);
        } else {
            this.updateAmount(currentAmount / 10L);
        }
        this.updateKeyStates();
    }

    public void onCancelClicked() {
    }

    public void onClearClicked() {
        updateAmount(0L);
        updateKeyStates();
    }

    public void onClearLongPressed() {
        updateAmount(0L);
        updateKeyStates();
    }

    public void onDecimalClicked() {
    }

    public void onDigitClicked(int digit) {
        long currentAmount = this.getAmount();
        if (currentAmount < this.maxAmount) {
            long newAmount = 10L * currentAmount + digit;
            this.updateAmount(Math.min(this.maxAmount, newAmount));
        }

        this.updateKeyStates();
    }

    public void onPinDigitEntered(float x, float y) {
    }

    public void onSkipClicked() {
    }

    protected boolean submitEnabled() {
        return true;
    }

    protected void updateKeyStates() {
        updateBackspaceState();
        updateSubmitState();
        updateDigitsState();
    }
}
