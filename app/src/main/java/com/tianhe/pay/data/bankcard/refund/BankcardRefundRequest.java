package com.tianhe.pay.data.bankcard.refund;

import com.tianhe.pay.data.bankcard.BankcardConstant;

public class BankcardRefundRequest {
    int transType;              // 交易类型
    String operatorNo;          // 操作员编号
    long amount;                // 退货金额(分)
    String oriReferenceNo;      // 原通莞系统参考号
    String outOrderNo;          // 外部订单号(天和订单号)
    String oriDate;             // 原交易日期(0205, 表示2月5号)
    boolean isOpenAdminVerify;  // 是否显示主管密码
    String traceNo;             //交易参考号（当日撤销）

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public BankcardRefundRequest(int type) {
        if(type == 1){
            transType = BankcardConstant.TransactionType.REVOKE;
        }else if(type == 2) {
            transType = BankcardConstant.TransactionType.REFUND;
        }
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getOriReferenceNo() {
        return oriReferenceNo;
    }

    public void setOriReferenceNo(String oriReferenceNo) {
        this.oriReferenceNo = oriReferenceNo;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getOriDate() {
        return oriDate;
    }

    public void setOriDate(String oriDate) {
        this.oriDate = oriDate;
    }

    public boolean isOpenAdminVerify() {
        return isOpenAdminVerify;
    }

    public void setOpenAdminVerify(boolean openAdminVerify) {
        isOpenAdminVerify = openAdminVerify;
    }
}
