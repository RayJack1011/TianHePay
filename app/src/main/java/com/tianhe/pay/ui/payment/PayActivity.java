package com.tianhe.pay.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tianhe.pay.R;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeActivity;
import com.tianhe.pay.ui.payment.bankcard.BankcardPayFragment;
import com.tianhe.pay.ui.payment.cash.CashPayFragment;
import com.tianhe.pay.ui.payment.coupon.CouponPayFragment;
import com.tianhe.pay.ui.payment.pos.PosPayFragment;
import com.tianhe.pay.ui.payment.storedvalue.StoredValueCardPayFragment;
import com.tianhe.pay.ui.payment.wechatali.WechatAliPayFragment;

public class PayActivity extends TianHeActivity {

    private static final String EXTRA_PAYMENT = "payment";

    public static Intent getStartIntent(Context context, Payment payment) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(EXTRA_PAYMENT, payment);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createSingleFragment() {
        Payment payment = getUsingPayment();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(payment.getPaymentId());
        PayFragment payFragment = null;
        switch (signpost) {
            case WECHAT:
            case ALI:
                payFragment = new WechatAliPayFragment();
                break;
            case STOREDVALUE_CARD:
                payFragment = new StoredValueCardPayFragment();
                break;
            case COUPON:
                payFragment = new CouponPayFragment();
                break;
            case BANKCARD:
                payFragment = new BankcardPayFragment();
                break;
            case CASH:
                payFragment = new CashPayFragment();
                break;
            case WECHAT_POS:
            case ALI_POS:
            case BANKCARD_POS:
                payFragment = new PosPayFragment();
                break;
        }
        payFragment.bindPayment(payment);
        return payFragment;
    }

    private Payment getUsingPayment() {
        return (Payment)getIntent().getSerializableExtra(EXTRA_PAYMENT);
    }
}
