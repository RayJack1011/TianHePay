package com.tianhe.pay.ui.refund;

import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface RefundOrderContract {

    interface View extends BaseView {

        void prepareSaleNoSuccess();

        void prepareSaleNoFail(String reason);

        void refundOrderSuccess(SubmitOrderResult result);

        void refundOrderFail(String reason);

        void printRefundOrderSuccess();

        void printRefundOrderFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void prepareSaleNo();

        void refundOrder();

        void printRefundOrder();
    }
}
