package com.tianhe.pay.model;

import com.tianhe.pay.R;

public enum PaymentSignpost {
//    WECHAT(R.drawable.ic_wechat, "8005"),
//    ALI(R.drawable.ic_ali, "8007"),
//    BANKCARD(R.drawable.ic_bankcard, "5006"),
//    COUPON(R.drawable.ic_coupon, "4001"),
//    STOREDVALUE_CARD(R.drawable.ic_bankcard, "6001"),
//    CASH(R.drawable.ic_storedvalue_card, "1001"),
//    WECHAT_POS(R.drawable.ic_wechat, "9004"),           // 微信离线(相当于补录)
//    ALI_POS(R.drawable.ic_ali, "80070"),                 // 支付宝离线(相当于补录)
//    BANKCARD_POS(R.drawable.ic_bankcard, "5099");       // 银行卡离线(相当于补录)

    WECHAT(R.drawable.ic_wechat, "03"),
    ALI(R.drawable.ic_ali, "02"),
    BANKCARD(R.drawable.ic_bankcard, "04"),
    COUPON(R.drawable.ic_coupon, "01"),
    STOREDVALUE_CARD(R.drawable.ic_bankcard, "6001"),
    CASH(R.drawable.ic_storedvalue_card, "1001"),
    WECHAT_POS(R.drawable.ic_wechat, "07"),           // 微信离线(相当于补录)
    ALI_POS(R.drawable.ic_ali, "05"),                 // 支付宝离线(相当于补录)
    BANKCARD_POS(R.drawable.ic_bankcard, "06");       // 银行卡离线(相当于补录)

    private int icon;
    private String paymentId;

    PaymentSignpost(int icon, String paymentId) {
        this.icon = icon;
        this.paymentId = paymentId;
    }

    public int getIcon() {
        return icon;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public static PaymentSignpost fromPaymentId(String paymentId) {
        for (PaymentSignpost signpost : PaymentSignpost.values()) {
            if (signpost.paymentId.equals(paymentId)) {
                return signpost;
            }
        }
        throw new IllegalArgumentException("unknown payment: paymentId=" + paymentId);
    }
}
