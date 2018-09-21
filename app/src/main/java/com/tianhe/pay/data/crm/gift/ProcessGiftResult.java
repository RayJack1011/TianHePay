package com.tianhe.pay.data.crm.gift;

import com.tianhe.pay.data.crm.CrmDataName;

import java.util.List;

/**
 * Crm赠品处理的结果
 */
public class ProcessGiftResult {
    @CrmDataName("mmaq_t")
    List<GiftCard> giftCards;

    @CrmDataName("gcao_q")
    List<GiftCoupon> giftCoupons;

    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

    public List<GiftCoupon> getGiftCoupons() {
        return giftCoupons;
    }

    public void setGiftCoupons(List<GiftCoupon> giftCoupons) {
        this.giftCoupons = giftCoupons;
    }
}
