package com.tianhe.pay.data.bankcard.revoke;

public class BankcardRevokeResponse {
    int transType;              // 交易类型
    String responseCode;         // 应答码
    String message;             // 失败消息
    long transAmount;           // 实际退货金额
    String transTime;           // 交易时间
    String cardNo;              // 卡号
    String voucherNo;           // 流水号
    String batchNo;             // 批次号
    String referenceNo;         // 通莞系统参考号
    String oriVoucherNo;        // 原交易流水号
    String channelId;           // 渠道ID

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(long transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getOriVoucherNo() {
        return oriVoucherNo;
    }

    public void setOriVoucherNo(String oriVoucherNo) {
        this.oriVoucherNo = oriVoucherNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
