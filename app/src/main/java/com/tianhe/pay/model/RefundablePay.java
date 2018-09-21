package com.tianhe.pay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 可退款的支付方式
 */
public class RefundablePay implements Serializable {
    private PaymentSignpost signpost;
    private String paymentName;
    private long refundedTotal;
    private long refundableTotal;
    private boolean refundable;
    private List<String> limitRelNos;    // 限定的退款关联号(例如退券时,只能退原券号)
    private long limitRefundMax;         // 限定的最大退款金额

    public PaymentSignpost getSignpost() {
        return signpost;
    }

    public void setSignpost(PaymentSignpost signpost) {
        this.signpost = signpost;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public long getRefundedTotal() {
        return refundedTotal;
    }

    public void setRefundedTotal(long refundedTotal) {
        this.refundedTotal = refundedTotal;
    }

    public long getRefundableTotal() {
        return refundableTotal;
    }

    public void setRefundableTotal(long refundableTotal) {
        this.refundableTotal = refundableTotal;
    }

    public void addRefundableTotal(long refundableAmount) {
        this.refundableTotal += refundableAmount;
    }


    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public List<String> getLimitRelNos() {
        return limitRelNos;
    }

    public void setLimitRelNos(List<String> limitRelNos) {
        this.limitRelNos = limitRelNos;
    }

    public void addLimitRelNo(String limitRelNo) {
        if (limitRelNos == null) {
            limitRelNos = new ArrayList<>();
        }
        limitRelNos.add(limitRelNo);
    }

    public boolean inRefundableLimits(String relNo) {
        if (limitRelNos == null) {
            return true;
        }
        return limitRelNos.contains(relNo);
    }

    public long getLimitRefundMax() {
        return limitRefundMax;
    }

    public void setLimitRefundMax(long limitRefundMax) {
        this.limitRefundMax = limitRefundMax;
    }

    public void changeRefunded(long refundedAmount) {
        this.refundedTotal += refundedAmount;
        this.refundableTotal -= refundedAmount;
    }

}
