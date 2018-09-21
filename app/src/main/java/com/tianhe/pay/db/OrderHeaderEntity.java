package com.tianhe.pay.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable
public class OrderHeaderEntity {
    @DatabaseField
    private String shopNo;
    @DatabaseField
    private String systemVersion;
    @DatabaseField
    private String userNo;
    @DatabaseField
    private String terminalId;
    @DatabaseField
    private String vipNo;
    @DatabaseField
    private String vipLevel;
    @DatabaseField
    private String uuid;
    @DatabaseField(id = true)
    private String saleNo;
    @DatabaseField
    private String saleNoSource;
    @DatabaseField
    private String time;
    @DatabaseField
    private String businessDate;
    @DatabaseField
    private boolean refunded;
    @DatabaseField
    private String isPractice;

    @ForeignCollectionField(eager = true)
    private Collection<OrderItemEntity> details;
    @ForeignCollectionField(eager = true)
    private Collection<PaidInfoEntity> payInfos;

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

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
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

    public Collection<OrderItemEntity> getDetails() {
        return details;
    }

    public void setDetails(Collection<OrderItemEntity> details) {
        this.details = details;
    }

    public Collection<PaidInfoEntity> getPayInfos() {
        return payInfos;
    }

    public void setPayInfos(Collection<PaidInfoEntity> payInfos) {
        this.payInfos = payInfos;
    }

}
