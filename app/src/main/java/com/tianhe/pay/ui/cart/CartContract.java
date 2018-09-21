package com.tianhe.pay.ui.cart;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface CartContract {
    interface View extends BaseView {
    }

    interface Presenter extends SavablePresenter<View> {
    }
}
