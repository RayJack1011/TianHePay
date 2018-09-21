package com.tianhe.pay.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.ui.TianHeActivity;

import javax.inject.Inject;

public class RefundOrderActivity extends TianHeActivity {
    private static final String EXTRA_ORDER = "order";

    public static Intent getStartIntent(Context context, Order order) {
        Intent intent = new Intent(context, RefundOrderActivity.class);
        intent.putExtra(EXTRA_ORDER, order);
        return intent;
    }

    @Inject
    RefundDataManager refundDataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle("原单退货");
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
    public void onBackPressed() {
        if (refundDataManager.isRefunding()) {
            showCancelRefundDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected Fragment createSingleFragment() {
        return RefundOrderFragment.newInstance(getOrderSource());
    }

    private Order getOrderSource() {
        return (Order) getIntent().getSerializableExtra(EXTRA_ORDER);
    }

    private void showCancelRefundDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(R.string.order_history_refund_cancel_title);
        builder.content(R.string.order_history_refund_cancel_with_paid);
        builder.negativeText(R.string.order_history_refund_cancel_no);
        builder.positiveText(R.string.order_history_refund_cancel_yes);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                clearRefund();
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void clearRefund() {
        refundDataManager.clear();
    }
}
