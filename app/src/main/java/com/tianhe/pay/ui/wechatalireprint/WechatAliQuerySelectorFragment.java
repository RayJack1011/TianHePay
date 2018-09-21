package com.tianhe.pay.ui.wechatalireprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.returncoupon.ReturnCouponActivity;

public class WechatAliQuerySelectorFragment extends TianHeFragment {

    View wechatSelect;
    View alitSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wechatali_reprint_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wechatSelect = view.findViewById(R.id.view_card_wechat);
        wechatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommomData.signName = "微信";
                navToQuery(PaymentSignpost.WECHAT);
            }
        });
        alitSelect = view.findViewById(R.id.view_card_alipay);
        alitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommomData.signName = "支付宝";
                navToQuery(PaymentSignpost.ALI);
            }
        });
        view.findViewById(R.id.view_card_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ReturnCouponActivity.class));
            }
        });
    }

    private void navToQuery(PaymentSignpost signpost) {
        nav.enterQueryWechatAli(this, signpost);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }
}
