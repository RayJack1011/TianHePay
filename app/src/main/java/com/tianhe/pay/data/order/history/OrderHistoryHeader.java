package com.tianhe.pay.data.order.history;

import com.google.gson.annotations.SerializedName;

/**
 * 历史订单中的头部信息
 */
public class OrderHistoryHeader {
    @SerializedName("ORGANIZATIONNO")
    private String shopNo;
    @SerializedName("VER_NUM")
    private String systemVersion;
    @SerializedName("OPNO")
    private String userNo;
    @SerializedName("MACHINE")
    private String terminalId;
    @SerializedName("CARDNO")
    private String vipNo;
    @SerializedName("POINT_QTY")
    private String points;      // 积分数

    @SerializedName("SALENO")
    private String saleNo;
    @SerializedName("OFNO")
    private String saleNoSource;
    @SerializedName("SDATE")
    private String date;
    @SerializedName("STIME")
    private String time;
    @SerializedName("ISPRACTICE")
    private String isPractice;

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getSaleNoSource() {
        return saleNoSource;
    }

    public void setSaleNoSource(String saleNoSource) {
        this.saleNoSource = saleNoSource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsPractice() {
        return isPractice;
    }

    public void setIsPractice(String isPractice) {
        this.isPractice = isPractice;
    }
}
