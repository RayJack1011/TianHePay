package com.tianhe.pay.ui.wechatalireprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tianhe.pay.R;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.ui.TianHeActivity;

public class WechatAliReprintActivity extends TianHeActivity {

    private static final String EXTRA_WECHAT_ALI_ORDER = "order";

    public static Intent getStartIntent(Context context, WechatAliOrder order) {
        Intent intent = new Intent(context, WechatAliReprintActivity.class);
        intent.putExtra(EXTRA_WECHAT_ALI_ORDER, order);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle("重打印详情");
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

    private WechatAliOrder getWechatAli() {
        return (WechatAliOrder) getIntent().getSerializableExtra(EXTRA_WECHAT_ALI_ORDER);
    }

    @Override
    protected Fragment createSingleFragment() {
        return WechatAliReprintFragment.newInstance(getWechatAli());
    }
}
