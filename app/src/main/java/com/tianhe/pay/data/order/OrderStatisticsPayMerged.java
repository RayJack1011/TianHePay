package com.tianhe.pay.data.order;

import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;

/**
 * 合并后的支付方式统计信息
 */
public class OrderStatisticsPayMerged implements Serializable {

    private String paymentId;
    private String paymentName;
    private Money saleTotals;
    private Money refundTotals;

    public OrderStatisticsPayMerged() {
        setSaleTotals(Money.zeroMoney());
        setRefundTotals(Money.zeroMoney());
    }

    public Money getRefundTotals() {
        return refundTotals;
    }

    public void setRefundTotals(Money refundTotals) {
        this.refundTotals = refundTotals;
    }

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

    public Money getSaleTotals() {
        return saleTotals;
    }

    public void setSaleTotals(Money amount) {
        this.saleTotals = amount;
    }

}
