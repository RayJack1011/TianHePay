package com.tianhe.pay.data.order;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;

/**
 * 支付方式统计信息
 */
public class OrderStatisticsPay implements Serializable {
    private static final String TYPE_SALE = "0";
    private static final String TYPE_REFUND = "5";

    @SerializedName("PAYCODE")
    private String paymentId;
    @SerializedName("PAYNAME")
    private String paymentName;
    @SerializedName("SUM(T.INPUTPAY)")
    private Money amount;
    @SerializedName("TYPE")
    private String type;

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

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public boolean isRefund(){
        return TYPE_REFUND.equals(type);
    }
}
