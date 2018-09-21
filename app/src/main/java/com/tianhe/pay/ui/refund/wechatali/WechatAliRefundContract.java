package com.tianhe.pay.ui.refund.wechatali;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface WechatAliRefundContract {

    interface View extends BaseView {

        void refundSuccess(PaidInfo refund);

        void refundFailed(String reason);

        void printRefundSuccess();

        void printRefundFail(String reason);
        void printRefundFail(String reason,PaidInfo refund);

    }

    interface Presenter extends SavablePresenter<View> {

        void refund(String refundCode, String paymentId);

        void printRefund(PaidInfo refund);
    }
}
