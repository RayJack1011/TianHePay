package com.tianhe.pay.data.crm.gift;

import com.tianhe.pay.data.crm.CrmDataName;

/**
 * 赠卡(状态变更).
 */
public class GiftCard {
    /** 卡状态: "2" - 发卡, "7" - 注销 */
    @CrmDataName("mmaq006")
    String cardStatus;

    /** 卡号 */
    @CrmDataName("mmaq001")
    String cardNo;

    /** 卡种编号 */
    @CrmDataName("mmaq002")
    String cardType;

    @CrmDataName("mmaq004")
    String password;

    /** (磁条中读出的)校验码 */
    @CrmDataName("mmaqua001")
    String verifyCode;

    /** 储值金额(异动金额) */
    @CrmDataName("mmau009")
    String amount;

    /** 实际储值金额 */
    @CrmDataName("mmau009_1")
    String realAmount;

    /** 送抵现值 */
    @CrmDataName("mmau013")
    String giftAmount;

    /** 储值成本 */
    @CrmDataName("mmau014")
    String cost;

    /** 发卡方式："2" - 销售赠卡 (赠送成功后返回)*/
    @CrmDataName("mmaq043")
    String giftMode;

    /** 折扣金额(赠送成功后返回) */
    @CrmDataName("mmau011")
    String discountAmount;

    /** 加值金额(赠送成功后返回) */
    @CrmDataName("mmau012")
    String appendAmount;

    @CrmDataName("mmaq100")
    String uuid;

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getGiftMode() {
        return giftMode;
    }

    public void setGiftMode(String giftMode) {
        this.giftMode = giftMode;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getAppendAmount() {
        return appendAmount;
    }

    public void setAppendAmount(String appendAmount) {
        this.appendAmount = appendAmount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
