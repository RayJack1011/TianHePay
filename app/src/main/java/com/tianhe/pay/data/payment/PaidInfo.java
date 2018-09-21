package com.tianhe.pay.data.payment;


import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.Skip;
import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;

/**
 * 支付信息
 */
public class PaidInfo implements Serializable {
    @SerializedName("paymodeId")
    String paymentId;
    @Skip
    String paymentName;
    String billNo;
//    String relationNumber;      // 关联单号(微信/支付宝单号, 银行参考号)
    Money saleAmount;
    transient String pwd;       // (储值卡/现金卡)密码
    transient String markCode;  // (储值卡/现金卡)校验码
    String time;                // 支付时间
    String cardNo;
    String cttype;              //券种，只有在使用券才进行赋值


    public String getCttype() {
        return cttype;
    }

    public void setCttype(String cttype) {
        this.cttype = cttype;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Money getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Money saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

//    public String getRelationNumber() {
//        return relationNumber;
////    }

//    public void setRelationNumber(String relationNumber) {
//        this.relationNumber = relationNumber;
//    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
