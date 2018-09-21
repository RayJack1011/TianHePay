package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.crm.CrmDataName;

import java.util.List;

/**
 * Crm称为"支付处理"结果
 */
public class ProcessPaymentResult {
    @CrmDataName("mmau_t_8")
    List<StoredValueCardPay>  storedValueCardPays;  // 储值卡支付列表
    @CrmDataName("mmar_t")
    List<PointProcess> pointProcesses;        // 对应的卡产生的积分
    @CrmDataName("gcao_t")
    List<CouponProcess> coupons;    // 券的使用信息

    public List<StoredValueCardPay> getStoredValueCardPays() {
        return storedValueCardPays;
    }

    public void setStoredValueCardPays(List<StoredValueCardPay> storedValueCardPays) {
        this.storedValueCardPays = storedValueCardPays;
    }

    public List<PointProcess> getPointProcesses() {
        return pointProcesses;
    }

    public void setPointProcesses(List<PointProcess> pointProcesses) {
        this.pointProcesses = pointProcesses;
    }

    public List<CouponProcess> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponProcess> coupons) {
        this.coupons = coupons;
    }

}
