package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;

public class CouponProcess {
    @CrmDataName("gcao005")
    String couponState;     // 券流转状态
    @CrmDataName("gcao001_1")
    String startCouponNo;   // 开始券号
    @CrmDataName("gcao001_2")
    String endCouponNo;     // 结束券号
    String uuid;

    public String getCouponState() {
        return couponState;
    }

    public void setCouponState(String couponState) {
        this.couponState = couponState;
    }

    public String getStartCouponNo() {
        return startCouponNo;
    }

    public void setStartCouponNo(String startCouponNo) {
        this.startCouponNo = startCouponNo;
    }

    public String getEndCouponNo() {
        return endCouponNo;
    }

    public void setEndCouponNo(String endCouponNo) {
        this.endCouponNo = endCouponNo;
    }

    public void setUsedState() {
        couponState = CrmConstants.STATE_COUPON_USED;
    }

    public void setCreateState() {
        couponState = CrmConstants.STATE_COUPON_CREATE;
    }

    public void setRefundState() {
        couponState = CrmConstants.STATE_COUPON_CREATE;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
