package com.tianhe.pay.common;

public interface SavablePresenter<V extends BaseView> extends BasePresenter {

    void bindView(V view);

    void restore(StatesContainer<String, Object> saved);

    void save(StatesContainer<String, Object> out);

}
