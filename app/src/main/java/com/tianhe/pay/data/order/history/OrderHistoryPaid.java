package com.tianhe.pay.data.order.history;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.utils.money.Money;

/**
 * 历史订单中的支付信息
 */
public class OrderHistoryPaid {
    @SerializedName("PAYCODE")
    String paymentId;
    @SerializedName("PAYNAME")
    String paymentName;
    @SerializedName("PAYSERNUM")
    String relationNumber;      // 关联单号(微信/支付宝单号, 银行参考号)
    @SerializedName("PAY")
    Money saleAmount;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getRelationNumber() {
        return relationNumber;
    }

    public void setRelationNumber(String relationNumber) {
        this.relationNumber = relationNumber;
    }

    public Money getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Money saleAmount) {
        this.saleAmount = saleAmount;
    }
}
