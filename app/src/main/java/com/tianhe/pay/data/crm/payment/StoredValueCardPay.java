package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.crm.CrmDataName;

public class StoredValueCardPay {
    @CrmDataName("mmau001")
    String cardNo;          // 卡号
    @CrmDataName("mmaq004")
    String password;        // 密码
    @CrmDataName("mmaqua001")
    String verifyCode;      // 校验码
    @CrmDataName("mmau009")
    String amount;          // 支付金额
    @CrmDataName("mmau100")
    String uuid;            // 唯一编号

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
