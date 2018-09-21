package com.tianhe.pay.data.order.calculate;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.order.OrderItem;

import java.util.List;

public class CalculationResult {
    @SerializedName("couponlist")
    List<Gift> couponList;
    @SerializedName("cardlist")
    List<Gift> cardList;

    PaymentLimit limitCard;
    @SerializedName("limitCoupon")
    List<PaymentLimit> limitCoupon;

    @SerializedName("saleDList")
    List<OrderItem> adjustedItems;

    public List<Gift> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Gift> couponList) {
        this.couponList = couponList;
    }

    public List<Gift> getCardList() {
        return cardList;
    }

    public void setCardList(List<Gift> cardList) {
        this.cardList = cardList;
    }

    public PaymentLimit getLimitCard() {
        return limitCard;
    }

    public void setLimitCard(PaymentLimit limitCard) {
        this.limitCard = limitCard;
    }

    public List<PaymentLimit> getLimitCoupon() {
        return limitCoupon;
    }

    public void setLimitCoupon(List<PaymentLimit> limitCoupon) {
        this.limitCoupon = limitCoupon;
    }

    public List<OrderItem> getAdjustedItems() {
        return adjustedItems;
    }

    public void setAdjustedItems(List<OrderItem> adjustedItems) {
        this.adjustedItems = adjustedItems;
    }
}
