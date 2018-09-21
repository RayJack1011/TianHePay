package com.tianhe.pay.ui.payment.wechatali;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.utils.money.Money;

public interface WechatAliPayContract {

    interface View extends BaseView {
        /**
         * 提示收银员, 正在等待用户付款.
         */
        void waitingCustomerPay();
        /**
         * 支付请求超时 查询中
         */
        void waitingQuary();
        /**
         * 消息提示
         * @param msg
         */
        void waitingForPay(String msg);

        void paySuccess(PaidInfo paidInfo);

        void payFailed(String reason);

        void printSuccess();

        void printFail(String reason);
        void printFail(String reason,PaidInfo paidInfo);


    }

    interface Presenter extends SavablePresenter<View> {

        void pay(Payment payment, Money payAmount, String relationNum);

        /**
         * 当买家长时间不付款, 主动取消等待
         */
        void cancelWaiting();

        void print(PaidInfo paidInfo);
    }
}
