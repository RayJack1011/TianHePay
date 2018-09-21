package com.tianhe.pay.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tianhe.pay.R;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeActivity;
import com.tianhe.pay.ui.refund.bankcard.BankcardRefundPayFragment;
import com.tianhe.pay.ui.refund.cash.CashRefundPayFragment;
import com.tianhe.pay.ui.refund.coupon.CouponRefundPayFragment;
import com.tianhe.pay.ui.refund.pos.PosRefundPayFragment;
import com.tianhe.pay.ui.refund.storedvalue.StoredValueCardRefundPayFragment;
import com.tianhe.pay.ui.refund.wechatali.WechatAliRefundPayFragment;

import javax.inject.Inject;

public class RefundPayActivity extends TianHeActivity {

    private static final String EXTRA_REFUND_PAY = "refundPay";

    public static Intent getStartIntent(Context context, PaymentSignpost signpost) {
        Intent intent = new Intent(context, RefundPayActivity.class);
        intent.putExtra(EXTRA_REFUND_PAY, signpost);
        return intent;
    }

    @Inject
    RefundDataManager refundDataManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findToolbar(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment createSingleFragment() {
        PaymentSignpost signpost = getPendingRefund();
        Fragment fragment = null;
        switch (signpost) {
            case WECHAT:
            case ALI:       // 微信支付/支付宝走相同渠道
                fragment = new WechatAliRefundPayFragment();
                break;
            case BANKCARD:
                fragment = new BankcardRefundPayFragment();
                break;
            case COUPON:
                fragment = new CouponRefundPayFragment();
                break;
            case STOREDVALUE_CARD:
                fragment = new StoredValueCardRefundPayFragment();
                break;
            case CASH:
                fragment =  new CashRefundPayFragment();
                break;
            case ALI_POS:
            case WECHAT_POS:
            case BANKCARD_POS:
                fragment = new PosRefundPayFragment();
            default:
                break;
        }
        if (fragment != null) {
            fragment.getArguments().putSerializable("refundable", signpost);
        }
        return fragment;
    }

    private PaymentSignpost getPendingRefund() {
        return (PaymentSignpost) getIntent().getSerializableExtra(EXTRA_REFUND_PAY);
    }

}
