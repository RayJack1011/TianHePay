package com.tianhe.pay.di.module;

import com.tianhe.pay.di.PerActivity;
import com.tianhe.pay.ui.auth.QueryAuthActivity;
import com.tianhe.pay.ui.cart.CartActivity;
import com.tianhe.pay.ui.cart.CartDetailActivity;
import com.tianhe.pay.ui.checkout.CheckoutActivity;
import com.tianhe.pay.ui.crm.QueryCrmActivity;
import com.tianhe.pay.ui.home.HomeActivity;
import com.tianhe.pay.ui.login.LoginActivity;
import com.tianhe.pay.ui.order.OrderCountActivity;
import com.tianhe.pay.ui.order.OrderHistoryActivity;
import com.tianhe.pay.ui.order.OrderHistoryDetailActivity;
import com.tianhe.pay.ui.order.OrderQueryActivity;
import com.tianhe.pay.ui.payment.PayActivity;
import com.tianhe.pay.ui.refund.RefundOrderActivity;
import com.tianhe.pay.ui.refund.RefundPayActivity;
import com.tianhe.pay.ui.setting.SettingActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQueryActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQuerySelectorActivity;
import com.tianhe.pay.ui.wechatalireprint.WechatAliReprintActivity;
import com.tianhe.pay.ui.welcome.WelcomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract WelcomeActivity contributesWelcomeActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract LoginActivity contributesLoginActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract HomeActivity contributesHomeActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract CheckoutActivity contributesCheckoutActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract PayActivity contributesPayActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract QueryCrmActivity contributesQueryCrmActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract CartActivity contributesCartActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract CartDetailActivity contributesCartDetailActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract OrderHistoryActivity contributesOrderHistoryActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract OrderQueryActivity contributesOrderQueryActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract OrderHistoryDetailActivity contributesOrderHistoryDetailActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract QueryAuthActivity contributesQueryAuthActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract RefundOrderActivity contributesRefundOrderActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract RefundPayActivity contributesRefundPaidActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract SettingActivity contributesSettingActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract OrderCountActivity contributesOrderTodayCountActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract WechatAliQueryActivity contributesWechatAliQueryActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract WechatAliReprintActivity contributesWechatAliReprintActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract WechatAliQuerySelectorActivity contributesWechatAliQuerySelectorActivity();
}
