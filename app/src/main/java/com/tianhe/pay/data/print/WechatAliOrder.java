package com.tianhe.pay.data.print;

import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;

public class WechatAliOrder implements Serializable {
    String lowOrderId;              // 天和订单号
    String upOrderId;               // 通莞金服订单号
    Money payMoney;
    private String datetime;        // 交易时间
    String payName;
    String orderState;              // 消费/退货/
    boolean reprint;

    String shopNo;
    String userNo;
    String terminalNo;


    public String getLowOrderId() {
        return lowOrderId;
    }

    public void setLowOrderId(String lowOrderId) {
        this.lowOrderId = lowOrderId;
    }

    public String getUpOrderId() {
        return upOrderId;
    }

    public void setUpOrderId(String upOrderId) {
        this.upOrderId = upOrderId;
    }

    public Money getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Money payMoney) {
        this.payMoney = payMoney;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public boolean isReprint() {
        return reprint;
    }

    public void setReprint(boolean reprint) {
        this.reprint = reprint;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }
}
