package com.tianhe.pay.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OrderSimpleEntity {
    @DatabaseField(id = true)
    private String saleNo;
    @DatabaseField
    private String saleTime;
    @DatabaseField
    private String businessDate;
    @DatabaseField
    private boolean refund;
    @DatabaseField
    private String total;
    @DatabaseField
    private String discount;

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public boolean isRefund() {
        return refund;
    }

    public void setRefund(boolean refund) {
        this.refund = refund;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
