package com.tianhe.pay.ui.refund.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class CouponRefundPayFragment extends RefundPayFragment {
    private static final int REQUEST_QUERY_COUPON = 1002;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder.amountTv.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY_COUPON) {
            if (resultCode == Activity.RESULT_OK) {
                Coupon coupon = (Coupon) data.getSerializableExtra("data");
                refundCoupon(coupon);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    @Override
    protected boolean needRelNo() {
        return false;
    }

    @Override
    protected boolean canModifyRefundAmount() {
        return true;
    }

    @Override
    protected void refund() {
        CommomData.isCouponReturn = true;
        nav.enterQueryCrmCouponForResult(this, REQUEST_QUERY_COUPON);
    }

    private void refundCoupon(Coupon coupon) {
        String refunded = getModifyAmount();
        String couponNo = coupon.getCouponNo();
        PaidInfo source = refundDataManager.findRefundableOriByRelNo(couponNo);
        if (source == null) {
            showMessage("不是原交易单中券号!");
            return;
        }
//        if (Double.valueOf(refunded) > source.getSaleAmount().getYuan()) {
//            showMessage("退款金额超限, 该券最大退款金额: " + source.getSaleAmount().toString());
//            return;
//        }

        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(Money.createAsYuan(refunded));
        paidInfo.setBillNo(couponNo);
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setTime(Times.nowDate());
        paidInfo.setCttype(coupon.getCouponType());
        refundDataManager.addRefundPaid(paidInfo);
        refundSuccess(paidInfo);
    }

}
