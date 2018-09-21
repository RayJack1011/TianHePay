package com.tianhe.devices;

public interface CardReader {
    public static class CardInfo {
        public String trackOne;
        public String trackTwo;
        public String trackThree;
    }

    public static interface Callback {
        void openFail(String reason);
        void onPrepared();
        void onRead(CardInfo cardInfo);
    }

    void openReadCard(Callback callback);

    void closeReadCard();
}
