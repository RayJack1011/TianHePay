package com.tianhe.pay.data.crm.coupon;

import com.tianhe.pay.data.crm.CrmDataName;

import java.io.Serializable;

public class Coupon implements Serializable {
    /** 券信息中日期格式化的标准 */
    public static final String DATE_FROMATE = "yy/mm/dd";

    @CrmDataName("gcao002")
    private String couponType;      // 券种编号
    @CrmDataName("gcao001")
    private String couponNo;        // 券编号
    @CrmDataName("gcafl003")
    private String couponName;      // 券名称
    @CrmDataName("gcao004")
    private String amount;          // 券金额
    @CrmDataName("gcao008")
    private String startDate;       // 生效日期
    @CrmDataName("gcao009")
    private String endDate;         // 失效日期
    @CrmDataName("gcaf021")
    private String changable;       // (是否)允许找零
    @CrmDataName("gcaf023")
    private String changeLimit;     // 最大找零金额

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getChangable() {
        return changable;
    }

    public void setChangable(String changable) {
        this.changable = changable;
    }

    public String getChangeLimit() {
        return changeLimit;
    }

    public void setChangeLimit(String changeLimit) {
        this.changeLimit = changeLimit;
    }
}
