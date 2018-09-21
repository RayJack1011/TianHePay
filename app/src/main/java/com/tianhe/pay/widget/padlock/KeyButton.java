package com.tianhe.pay.widget.padlock;

public interface KeyButton {

    int descriptionId();

    int displayValueId();

    class One implements KeyButton {
        @Override
        public int descriptionId() {
            return 0;
        }

        @Override
        public int displayValueId() {
            return 0;
        }
    }
}
