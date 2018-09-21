package com.tianhe.pay.ui.home;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface HomeContract {
    interface View extends BaseView {
        void calculatedSuccess();

        void calculatedFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void calculateKaypadItem();

    }
}
