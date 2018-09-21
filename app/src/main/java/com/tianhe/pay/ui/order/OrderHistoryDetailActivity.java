package com.tianhe.pay.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.ui.TianHeActivity;

public class OrderHistoryDetailActivity extends TianHeActivity {

    private static final String EXTRA_ORDER = "order";
    private static final String EXTRA_PRINTABLE = "printable";

    public static Intent getStartIntent(Context context, Order order, boolean printable) {
        Intent intent = new Intent(context, OrderHistoryDetailActivity.class);
        intent.putExtra(EXTRA_ORDER, order);
        intent.putExtra(EXTRA_PRINTABLE, printable);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle("账单详情");
    }

    @Override
    protected Fragment createSingleFragment() {
        return OrderHistoryDetailFragment.newInstance(getOrderHistory(), isRefundOnly());
    }

    private Order getOrderHistory() {
        return (Order) getIntent().getSerializableExtra(EXTRA_ORDER);
    }


    private boolean isRefundOnly() {
        return getIntent().getBooleanExtra(EXTRA_PRINTABLE, false);
    }

}
