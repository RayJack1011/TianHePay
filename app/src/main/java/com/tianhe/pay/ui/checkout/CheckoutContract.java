package com.tianhe.pay.ui.checkout;

import com.tianhe.pay.data.crm.gift.GiftCoupon;
import com.tianhe.pay.data.crm.gift.ProcessGift;
import com.tianhe.pay.data.crm.gift.ProcessGiftResult;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

import java.util.List;

public interface CheckoutContract {
    interface View extends BaseView {

        void calculateSuccess();

        void calculateFail(String reason);

        void renderUsablePayments(List<Payment> payments);

        void loadUsablePaymentsFail(String reason);

        void submitOrderSuccess(SubmitOrderResult result);

        void submitOrderFail(String reason);

        void printOrderSuccess();

        void printOrderFail(String reason);
        void printOrderFail(String reason,SubmitOrderResult result);

        void processGiftSuccess(ProcessGiftResult gift);

        void processGiftFail(String message);
        void processGiftFail(String message,ProcessGiftResult gift);

        void printGiftCouponFail(String message);

        void printGiftCouponSuccess();
    }

    interface Presenter extends SavablePresenter<View> {
        void calculateOrder();

        void loadUsablePayment();

        void submitOrder();

        void printOrder();

        void processGift();

        // 打印纸质券
        void printGiftCoupon(List<GiftCoupon> giftCoupons);
    }
}
