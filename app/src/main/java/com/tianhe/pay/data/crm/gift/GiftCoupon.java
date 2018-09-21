package com.tianhe.pay.data.crm.gift;

import com.tianhe.pay.data.crm.CrmDataName;

/**
 * 生成赠券
 */
public class GiftCoupon {
    /** 流转状态: "4" - 发售 */
    @CrmDataName("gcao005")
    String status;

    /** 券种编号 */
    @CrmDataName("gcao002")
    String type;

    /** 数量 */
    @CrmDataName("gcbi005")
    String quantity;

    /** 券发行方式："2" - 消费赠券 (赠券成功后返回) */
    @CrmDataName("gcao006")
    String giftMode;

    /** 券面额(赠送成功后返回) */
    @CrmDataName("oocql004")
    String denomination;

    /** 券编号(赠送成功后返回) */
    @CrmDataName("gcao001")
    String couponNo;

    @CrmDataName("gcao100")
    String uuid;

    public String getStatus() {
        return status;
    }

    public void setPublishStatus() {
        this.status = "4";
    }

//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGiftMode() {
        return giftMode;
    }

    public void setGiftMode(String giftMode) {
        this.giftMode = giftMode;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }
}
