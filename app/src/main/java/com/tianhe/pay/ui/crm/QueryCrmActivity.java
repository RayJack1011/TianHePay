package com.tianhe.pay.ui.crm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tianhe.pay.R;
import com.tianhe.pay.ui.TianHeActivity;
import com.tianhe.pay.ui.crm.coupon.QueryCouponFragment;
import com.tianhe.pay.ui.crm.member.QueryMemberFragment;
import com.tianhe.pay.ui.crm.storedvaluecard.QueryStoredValueCardFragment;

import javax.inject.Inject;

public class QueryCrmActivity extends TianHeActivity {

    private static final int TYPE_MEMBER = 1;
    private static final int TYPE_STOREDVALUE_CARD = 2;
    private static final int COUPON = 3;

    public static Intent queryMember(Context context) {
        Intent intent = new Intent(context, QueryCrmActivity.class);
        intent.putExtra("type", TYPE_MEMBER);
        return intent;
    }

    public static Intent queryStoredValueCard(Context context) {
        Intent intent = new Intent(context, QueryCrmActivity.class);
        intent.putExtra("type", TYPE_STOREDVALUE_CARD);
        return intent;
    }

    public static Intent queryCoupon(Context context) {
        Intent intent = new Intent(context, QueryCrmActivity.class);
        intent.putExtra("type", COUPON);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        hideBar();
    }

    private void hideBar() {
        Toolbar toolbar = findToolbar(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected Fragment createSingleFragment() {
        int type = getIntent().getIntExtra("type", 0);
        Fragment fragment = null;
        switch (type) {
            case TYPE_MEMBER:
                fragment = new QueryMemberFragment();
                break;
            case TYPE_STOREDVALUE_CARD:
                fragment = new QueryStoredValueCardFragment();
                break;
            case COUPON:
                fragment = new QueryCouponFragment();
                break;
        }
        return fragment;
    }
}
