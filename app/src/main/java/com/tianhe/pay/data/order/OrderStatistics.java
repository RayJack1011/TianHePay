package com.tianhe.pay.data.order;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易统计
 */
public class OrderStatistics implements Serializable {
    /**
     * 统计日期
     */
    private String countDate;
    /**
     * 消费单数
     */
    private int saleCount;
    /**
     * 消费金额
     */
    @SerializedName("payAmount")
    private Money saleTotals;
    /**
     * 退货单数
     */
    @SerializedName("returnCount")
    private int refundCount;
    /**
     * 退货金额
     */
    @SerializedName("payAmountReturn")
    private Money refundTotal;
    /**
     * 支付方式统计信息
     */
    @SerializedName("list")
    private List<OrderStatisticsPay> payStatistics;

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }

    public Money getStatisticsTotal() {
        return saleTotals.subtract(refundTotal);
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public Money getSaleTotals() {
        return saleTotals;
    }

    public void setSaleTotals(Money saleTotals) {
        this.saleTotals = saleTotals;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public Money getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(Money refundTotal) {
        this.refundTotal = refundTotal;
    }

    public List<OrderStatisticsPay> getPayStatistics() {
        return payStatistics;
    }

    public void setPayStatistics(List<OrderStatisticsPay> payStatistics) {
        this.payStatistics = payStatistics;
    }

    /**
     * 根据支付方式ID, 合并支付方式消费和退款信息.
     * @return
     */
    public List<OrderStatisticsPayMerged> mergePayById() {
        if (payStatistics == null || payStatistics.size() == 0) {
            return Collections.emptyList();
        }
        Map<String, OrderStatisticsPayMerged> mergedMap = new HashMap<>();
        for (OrderStatisticsPay pay : payStatistics) {
            final String paymentId = pay.getPaymentId();
            OrderStatisticsPayMerged mergedPay = mergedMap.get(paymentId);
            if (mergedPay == null) {
                mergedPay = new OrderStatisticsPayMerged();
                mergedPay.setPaymentId(paymentId);
                mergedPay.setPaymentName(pay.getPaymentName());
                mergedMap.put(paymentId, mergedPay);
            }
            if (pay.isRefund()) {
                mergedPay.setRefundTotals(pay.getAmount());
            } else {
                mergedPay.setSaleTotals(pay.getAmount());
            }
        }
        return new ArrayList<>(mergedMap.values());
    }
}
