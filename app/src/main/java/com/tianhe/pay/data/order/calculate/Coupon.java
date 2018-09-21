package com.tianhe.pay.data.order.calculate;

/**
 * Created by wangya3 on 2018/3/15.
 */

public class Coupon {
    private int couponNum;
    private String couponType;

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public String getCouponType() {
        return couponType;
    }
}
