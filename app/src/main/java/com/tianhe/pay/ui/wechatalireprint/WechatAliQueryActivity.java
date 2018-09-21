package com.tianhe.pay.ui.wechatalireprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tianhe.pay.R;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeActivity;

public class WechatAliQueryActivity extends TianHeActivity {

    public static final String TYPE = "Pay_Type";

    public static Intent getStartIntent(Context context, PaymentSignpost signpost) {
        Intent intent = new Intent(context, WechatAliQueryActivity.class);
        intent.putExtra(TYPE, signpost);
        return intent;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        PaymentSignpost signpost = getSelectedPaymentSignpost();
        switch (signpost) {
            case ALI:
                setTitle("重打印支付宝账单");
                break;
            case WECHAT:
                setTitle("重打印微信支付账单");
                break;
            default:
                setTitle(R.string.drawer_left_reprint);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createSingleFragment() {
        WechatAliQueryFragment fragment = new WechatAliQueryFragment();
        fragment.bindPaymentSignpost(getSelectedPaymentSignpost());
        return fragment;
    }

    private PaymentSignpost getSelectedPaymentSignpost() {
        return (PaymentSignpost) getIntent().getSerializableExtra(TYPE);
    }
}
