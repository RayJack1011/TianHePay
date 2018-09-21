package com.tianhe.pay.common;

public interface BaseView {
    void showMessage(CharSequence message);

    void showMessage(int messageResId);

    StatesContainer<String, Object> getStatesContainer();
}
