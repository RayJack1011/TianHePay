package com.tianhe.pay.data.crm.coupon;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;
import com.tianhe.pay.data.crm.CrmRequest;

public class QueryCoupon extends CrmRequest {
    @CrmDataName("Type")
    String type = "1";      // 操作类型: 使用券前查询
    @CrmDataName("ooef001")
    String shopNo;          // 门店编号
    @CrmDataName("gcao001")
    String couponNo;        // 券编号
    public QueryCoupon(String shopNo) {
        super(CrmConstants.QUERY_SINGLE_COUPON, shopNo);
        this.shopNo = shopNo;
    }

    public void setRefund() {
        type = "2";         // 退款
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }
}
