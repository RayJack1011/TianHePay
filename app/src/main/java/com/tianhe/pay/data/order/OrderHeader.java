package com.tianhe.pay.data.order;

import java.io.Serializable;

public class OrderHeader implements Serializable {
    private String shopNo;
    private String systemVersion;
    private String userNo;
    private String terminalId;
    private String vipNo;
    private String vipLevel;
    private String uuid;
    private String saleNo;
    private String saleNoSource;
    private String time;
    private boolean refunded;
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

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public String getIsPractice() {
        return isPractice;
    }

    public void setIsPractice(String isPractice) {
        this.isPractice = isPractice;
    }

    public OrderHeader copy() {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setShopNo(shopNo);
        orderHeader.setSystemVersion(systemVersion);
        orderHeader.setUserNo(userNo);
        orderHeader.setTerminalId(terminalId);
        orderHeader.setVipNo(vipNo);
        orderHeader.setVipLevel(vipLevel);
        orderHeader.setUuid(uuid);
        orderHeader.setSaleNo(saleNo);
        orderHeader.setSaleNoSource(saleNoSource);
        orderHeader.setTime(time);
        orderHeader.setIsPractice(isPractice);
        return orderHeader;
    }
}
