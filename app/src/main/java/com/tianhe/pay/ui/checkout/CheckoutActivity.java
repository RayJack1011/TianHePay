package com.tianhe.pay.ui.checkout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.order.calculate.PaymentLimit;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.ui.TianHeActivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class CheckoutActivity extends TianHeActivity {

    @Inject
    CartManager cartManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //将券的衔接进行分组合并
        Map<String, BigDecimal> coupopLimitMap = new HashMap<String, BigDecimal>();
        List<PaymentLimit> list = CommomData.limitCoupon;
        if (list.size() > 0) {
            for (PaymentLimit paymentLimit : list) {
                String type = paymentLimit.getType();
                BigDecimal money = new BigDecimal(paymentLimit.getMoney());
                BigDecimal bigDecimal = coupopLimitMap.get(type);
                if (bigDecimal == null) {
                    coupopLimitMap.put(type, money);
                } else {
                    coupopLimitMap.put(type, bigDecimal.add(money));
                }
            }
            CommomData.coupopLimitMap = coupopLimitMap;
        }

        super.onCreate(savedInstanceState);
        setToolbar(R.id.toolbar);
        setTitle("订单支付");

    }

    @Override
    public void onBackPressed() {
        showEmptyCartDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showEmptyCartDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createSingleFragment() {
        return new CheckoutFragment();
    }

    private void showEmptyCartDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(R.string.checkout_cancel_order_title);
        if (cartManager.hasPaidInfo()) {
            builder.content(getString(R.string.checkout_cancel_order_with_paidInfo));
        } else {
            builder.content("已选购" + cartManager.getSaleCount() + "件商品");
        }
        builder.negativeText(R.string.checkout_cancel_order_no);
        builder.positiveText(R.string.checkout_cancel_order_yes);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                cancelOrder();
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

    private void cancelOrder() {
        cartManager.clear();
        finish();
    }
}
