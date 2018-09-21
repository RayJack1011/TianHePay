package com.tianhe.pay.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class PaidInfoEntity {
    @DatabaseField
    String paymentId;
    @DatabaseField
    String paymentName;
    @DatabaseField
    String relationNumber;          // 关联单号(微信/支付宝单号, 银行参考号)
    @DatabaseField
    String saleAmount;
    @DatabaseField
    String time;                    // 支付时间
    @DatabaseField
    private String businessDate;    // 所属日期

    @DatabaseField(foreign = true, foreignColumnName = "saleNo")
    private OrderHeaderEntity head;

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

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public OrderHeaderEntity getHead() {
        return head;
    }

    public void setHead(OrderHeaderEntity head) {
        this.head = head;
    }
}
