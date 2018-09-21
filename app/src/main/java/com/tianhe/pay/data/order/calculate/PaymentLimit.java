package com.tianhe.pay.data.order.calculate;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;

/**
 * 支付限定:接收券、接收卡、会员打折
 */
public class PaymentLimit {

    @SerializedName("couponType")
    String type;            // (卡/券的)种类
    String canPayCoupon;    // 能否收现金券
    String canPayCard;      // 能否通过储值卡支付
    @SerializedName("cANVIPAGIO")
    String canVipDiscount;  // 能否会员打折
    @SerializedName("money")
    String money;  // 接券金额

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean canUseStoredValueCard(StoredValueCard card) {
        if (type.equals(card.getCardType())) {
            return "Y".equals(canPayCard);
        }
        return true;
    }

    public boolean canUseCoupon(Coupon coupon) {
        if (type.equals(coupon.getCouponType())) {
            return "Y".equals(canPayCoupon);
        }
        return true;
    }

}
