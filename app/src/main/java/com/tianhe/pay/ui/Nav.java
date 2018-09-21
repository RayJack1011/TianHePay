package com.tianhe.pay.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tianhe.pay.data.bankcard.DataUtils;
import com.tianhe.pay.data.bankcard.refund.BankcardRefundRequest;
import com.tianhe.pay.data.bankcard.sale.BankcardSaleRequest;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.auth.QueryAuthActivity;
import com.tianhe.pay.ui.cart.CartActivity;
import com.tianhe.pay.ui.cart.CartDetailActivity;
import com.tianhe.pay.ui.checkout.CheckoutActivity;
import com.tianhe.pay.ui.crm.QueryCrmActivity;
import com.tianhe.pay.ui.home.HomeActivity;
import com.tianhe.pay.ui.login.LoginActivity;
import com.tianhe.pay.ui.modify.ModifyActivity;
import com.tianhe.pay.ui.order.OrderHistoryActivity;
import com.tianhe.pay.ui.order.OrderHistoryDetailActivity;
import com.tianhe.pay.ui.order.OrderQueryActivity;
import com.tianhe.pay.ui.order.OrderCountActivity;
import com.tianhe.pay.ui.payment.PayActivity;
import com.tianhe.pay.ui.refund.RefundOrderActivity;
import com.tianhe.pay.ui.refund.RefundPayActivity;
import com.tianhe.pay.ui.returncoupon.ReturnCouponActivity;
import com.tianhe.pay.ui.setting.SettingActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQueryActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQuerySelectorActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliReprintActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;
//import com.tianhe.zbar.ScanCaptureActivity;

public class Nav {

    private Context context;

    public Nav(Context context) {
        this.context = context;
    }

    public void enterLogin(Object trigger) {
        startActivity(trigger, LoginActivity.class);
    }

    public void enterModify(Object trigger){
        startActivity(trigger, ModifyActivity.class);
    }

    public void enterHome(Object trigger) {
        Intent intent = HomeActivity.getStartIntent(context);
        startActivity(trigger, intent);
    }

    /**
     * 虚拟的Pos补录进入购物车
     * @param trigger
     */
    public void enterHomeByPos(Object trigger) {
        Intent intent = HomeActivity.getStartIntentByPos(context);
        startActivity(trigger, intent);
    }

    public void enterScanBarForResult(Object trigger, String tipsMessage, int requestCode) {
        startActivityForResult(trigger, CaptureActivity.class, requestCode);
//        Intent intent = CaptureActivity.getStartIntent(context, tipsMessage);
//        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterCart(Object trigger) {
        startActivity(trigger, CartActivity.class);
    }

    public void enterCartDetail(Object trigger, int cartItemIndex) {
        Intent intent = CartDetailActivity.getStartIntent(context, cartItemIndex);
        startActivity(trigger, intent);
    }

    public void enterCheckout(Object trigger) {
        startActivity(trigger, CheckoutActivity.class);
    }

    public void enterPay(Object trigger, Payment payment) {
        Intent intent = PayActivity.getStartIntent(context, payment);
        startActivity(trigger, intent);
    }

    public void enterQueryCrmMemberForResult(Object trigger, int requestCode) {
        Intent intent = QueryCrmActivity.queryMember(context);
        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterQueryCrmCouponForResult(Object trigger, int requestCode) {
        Intent intent = QueryCrmActivity.queryCoupon(context);
        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterQueryCrmStoredValueCardForResult(Object trigger, int requestCode) {
        Intent intent = QueryCrmActivity.queryStoredValueCard(context);
        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterQueryAuthForResult(Object trigger, int requestCode) {
        startActivityForResult(trigger, QueryAuthActivity.class, requestCode);
    }

    public void enterMisPayForResult(Object trigger, int requestCode, BankcardSaleRequest request) {
        Intent intent = DataUtils.getStartMisPayIntent(request);
        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterMisRefundForResult(Object trigger, int requestCode, BankcardRefundRequest request) {
        Intent intent = DataUtils.getStartMisRefundIntent(request);
        startActivityForResult(trigger, intent, requestCode);
    }

    public void enterOrderHistory(Object trigger) {
        startActivity(trigger, OrderHistoryActivity.class);
    }

    public void enterOrderHistoryDetail(Object trigger, Order order, boolean printable) {
        Intent intent = OrderHistoryDetailActivity.getStartIntent(context, order, printable);
        startActivity(trigger, intent);
    }

    public void enterOrderHistoryDetail(Object trigger, Order order) {
        Intent intent = OrderHistoryDetailActivity.getStartIntent(context, order, true);
        startActivity(trigger, intent);
    }

    public void enterTodayStatistics(Object trigger) {
        startActivity(trigger, OrderCountActivity.class);
    }

    public void enterQueryRefundableOrder(Object trigger) {
        startActivity(trigger, OrderQueryActivity.class);
    }

    public void enterRefundOrder(Object trigger, Order order, int requestCode) {
        Intent intent = RefundOrderActivity.getStartIntent(context, order);
        startActivityForResult(trigger, intent, requestCode);
    }

    /** 退支付方式 */
    public void enterRefundPay(Object trigger, PaymentSignpost signpost) {
        Intent intent = RefundPayActivity.getStartIntent(context, signpost);
        startActivity(trigger, intent);
    }

    public void enterQueryWechatAli(Object trigger) {
        startActivity(trigger, WechatAliQueryActivity.class);
    }

    public void enterWechatAliQuerySelector(Object trigger) {
        startActivity(trigger, WechatAliQuerySelectorActivity.class);
    }

    public void enterQueryWechatAli(Object trigger, PaymentSignpost signpost) {
        Intent intent = WechatAliQueryActivity.getStartIntent(context, signpost);
        startActivity(trigger, intent);

    }

    public void enterReprintWechatAli(Object trigger, WechatAliOrder order) {
        Intent intent = WechatAliReprintActivity.getStartIntent(context, order);
        startActivity(trigger, intent);
    }

    public void enterSettings(Object trigger) {
        startActivity(trigger, SettingActivity.class);
    }
    public void enterCouponReturn(Object trigger) {
        startActivity(trigger, ReturnCouponActivity.class);
    }

    private static void startActivity(Object obj, Class<?> target) {
        if (obj instanceof Activity) {
            Activity activity = (Activity) obj;
            Intent intent = new Intent(activity, target);
            activity.startActivity(intent);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            Intent intent = new Intent(fragment.getActivity(), target);
            fragment.startActivity(intent);
        }
    }

    private static void startActivity(Object obj, Intent intent) {
        if (obj instanceof Activity) {
            Activity activity = (Activity) obj;
            activity.startActivity(intent);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            fragment.startActivity(intent);
        }
    }

    private static void startActivityForResult(Object obj, Class<?> target, int requestCode) {
        if (obj instanceof Activity) {
            Activity activity = (Activity) obj;
            Intent intent = new Intent(activity, target);
            activity.startActivityForResult(intent, requestCode);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            Intent intent = new Intent(fragment.getActivity(), target);
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    private static void startActivityForResult(Object obj, Intent intent, int requestCode) {
        if (obj instanceof Activity) {
            Activity activity = (Activity) obj;
            activity.startActivityForResult(intent, requestCode);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            fragment.startActivityForResult(intent, requestCode);
        }
    }
}
