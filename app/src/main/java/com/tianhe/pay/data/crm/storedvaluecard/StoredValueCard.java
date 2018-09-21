package com.tianhe.pay.data.crm.storedvaluecard;

import com.tianhe.pay.data.crm.CrmDataName;

import java.io.Serializable;

/**
 * 天和储值卡信息
 */
public class StoredValueCard implements Serializable {
    @CrmDataName("mmaq001")
    private String cardNo;
    @CrmDataName("mmaq002")
    private String cardType;
    @CrmDataName("mmaq005")
    private String validDate;       // 有效(截止)日期
    @CrmDataName("mmaq003")
    private String memberNo;        // 会员编号
    @CrmDataName("mmaf008")
    private String memberName;      // 会员姓名
    @CrmDataName("mmaq018")
    private String remainingPoint;  // 剩余积分
    @CrmDataName("mmaq009")
    private String remainingAmount; // 当前卡余额
    @CrmDataName("mman038")
    private String asCash;          // 是否可以抵现(Y/N)
    @CrmDataName("mman039")
    private String asCashMinLimit;  // 最低抵现金额
    @CrmDataName("mman042")
    private String storable;        // 是否可充值(Y/N)
    @CrmDataName("mman043")
    private String storableMulti;   // 是否可多次充值(Y/N)

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

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRemainingPoint() {
        return remainingPoint;
    }

    public void setRemainingPoint(String remainingPoint) {
        this.remainingPoint = remainingPoint;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getAsCash() {
        return asCash;
    }

    public void setAsCash(String asCash) {
        this.asCash = asCash;
    }

    public String getAsCashMinLimit() {
        return asCashMinLimit;
    }

    public void setAsCashMinLimit(String asCashMinLimit) {
        this.asCashMinLimit = asCashMinLimit;
    }

    public String getStorable() {
        return storable;
    }

    public void setStorable(String storable) {
        this.storable = storable;
    }

    public String getStorableMulti() {
        return storableMulti;
    }

    public void setStorableMulti(String storableMulti) {
        this.storableMulti = storableMulti;
    }
}
