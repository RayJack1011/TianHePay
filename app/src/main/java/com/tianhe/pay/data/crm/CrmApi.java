package com.tianhe.pay.data.crm;

import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.crm.coupon.QueryCoupon;
import com.tianhe.pay.data.crm.gift.ProcessGift;
import com.tianhe.pay.data.crm.gift.ProcessGiftResult;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.crm.member.QueryMember;
import com.tianhe.pay.data.crm.payment.ProcessPayment;
import com.tianhe.pay.data.crm.payment.ProcessPaymentResult;
import com.tianhe.pay.data.crm.storedvaluecard.QueryStoredValueCard;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;

import io.reactivex.Observable;

public interface CrmApi {
    /**
     * 查询会员卡信息
     * @param query
     * @return
     */
    Observable<Member> queryMember(QueryMember query);

    /**
     * 查询储值卡信息
     * @param query
     * @return
     */
    Observable<StoredValueCard> queryStoredValueCard(QueryStoredValueCard query);

    /**
     * 查询现金券(纸质券)信息
     * @param query
     * @return
     */
    Observable<Coupon> queryCoupon(QueryCoupon query);

    /**
     * 提交支付信息, 更新会员对应的数据
     * @param request
     * @return
     */
    Observable<ProcessPaymentResult> submitPayment(ProcessPayment request);


    Observable<ProcessGiftResult> submitGift(ProcessGift request);

}
