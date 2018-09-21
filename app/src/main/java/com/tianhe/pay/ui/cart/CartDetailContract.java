package com.tianhe.pay.ui.cart;

import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.utils.money.Money;

public interface CartDetailContract {
    interface View extends BaseView {
        void calculatedModifyItem(CalculationResult result);

        void calculateFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void calculateItem(int index, int quantity, Money price);
    }
}
