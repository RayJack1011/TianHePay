package com.tianhe.pay.data.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 支付方式
 */
public class Payment implements Serializable {
//    @SerializedName("PAYMENT_ID_")
//    String paymentId;
    @SerializedName("SHOP_NO_")
    String shopNo;
    @SerializedName("PAYNAME")
    String paymentName;
    @SerializedName("PAY_TYPE_")// 支付类型
    String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
