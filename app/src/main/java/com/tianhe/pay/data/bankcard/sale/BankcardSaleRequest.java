package com.tianhe.pay.data.bankcard.sale;

import com.tianhe.pay.data.bankcard.BankcardConstant;

public class BankcardSaleRequest {
    int transType;              // 交易类型
    String operatorNo;          // 操作员编号
    long amount;                // 金额（分）
    String outOrderNo;          // 外部订单号(天和订单号)

    public BankcardSaleRequest() {
        transType = BankcardConstant.TransactionType.SALE;
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

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }
}
