package com.tianhe.pay.data.order.history;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.money.Money;

/**
 * 同步交易数据时，返回的订单简略信息
 */
public class OrderSimple {
    @SerializedName("SALENO")
    String saleNo;
    @SerializedName("TOT_AMT")
    String time;
    @SerializedName("TRAN_TIME")
    Money saleTotal;
    @SerializedName("OFNO")
    String saleNoSource;

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Money getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(Money saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getSaleNoSource() {
        return saleNoSource;
    }

    public void setSaleNoSource(String saleNoSource) {
        this.saleNoSource = saleNoSource;
    }

    public boolean isRefund() {
        return !Strings.isBlank(saleNoSource);
    }
}
