package com.tianhe.pay.data.bankcard.revoke;

import com.tianhe.pay.data.bankcard.BankcardConstant;

public class BankcardRevokeRequest {
    int transType;              // 交易类型
    String operatorNo;          // 操作员编号
    String oriVoucherNo;        // 原交易流水号
    String oriTransTime;        // 原交易时间
    String outOrderNo;          // 外部订单号
    boolean isOpenAdminVerify;   // 是否显示主管密码

    public BankcardRevokeRequest() {
        transType = BankcardConstant.TransactionType.REVOKE;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getOriVoucherNo() {
        return oriVoucherNo;
    }

    public void setOriVoucherNo(String oriVoucherNo) {
        this.oriVoucherNo = oriVoucherNo;
    }

    public String getOriTransTime() {
        return oriTransTime;
    }

    public void setOriTransTime(String oriTransTime) {
        this.oriTransTime = oriTransTime;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public boolean isOpenAdminVerify() {
        return isOpenAdminVerify;
    }

    public void setOpenAdminVerify(boolean openAdminVerify) {
        isOpenAdminVerify = openAdminVerify;
    }
}
