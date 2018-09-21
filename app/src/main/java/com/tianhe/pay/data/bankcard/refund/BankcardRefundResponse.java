package com.tianhe.pay.data.bankcard.refund;

import com.tianhe.pay.data.bankcard.BankcardConstant;

public class BankcardRefundResponse {
    int transType;              // 交易类型
    String responseCode;         // 应答码
    String message;             // 失败消息
    long amount;                // 原退货金额
    long transAmount;           // 实际退货金额
    String transTime;           // 交易时间
    String cardNo;              // 卡号
    String voucherNo;           // 流水号
    String batchNo;             // 批次号
    String referenceNo;         // 通莞系统参考号
    String oriReferenceNo;      // 原通莞系统参考号
    String channelId;           // 渠道ID

    public boolean isSuccess() {
        return BankcardConstant.ResponseCode.SUCCESS.equals(responseCode);
    }

    public boolean isCancel() {
        return BankcardConstant.ResponseCode.CANCEL.equals(responseCode);
    }

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public String getOriReferenceNo() {
        return oriReferenceNo;
    }

    public void setOriReferenceNo(String oriReferenceNo) {
        this.oriReferenceNo = oriReferenceNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
