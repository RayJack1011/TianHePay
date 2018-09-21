package com.tianhe.pay.di.module;

import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.auth.QueryAuthFragment;
import com.tianhe.pay.ui.cart.CartDetailFragment;
import com.tianhe.pay.ui.cart.CartFragment;
import com.tianhe.pay.ui.checkout.CheckoutFragment;
import com.tianhe.pay.ui.crm.coupon.QueryCouponFragment;
import com.tianhe.pay.ui.crm.member.QueryMemberFragment;
import com.tianhe.pay.ui.crm.storedvaluecard.QueryStoredValueCardFragment;
import com.tianhe.pay.ui.home.HomeFragment;
import com.tianhe.pay.ui.login.LoginFragment;
import com.tianhe.pay.ui.order.OrderCountFragment;
import com.tianhe.pay.ui.order.OrderHistoryDetailFragment;
import com.tianhe.pay.ui.order.OrderHistoryFragment;
import com.tianhe.pay.ui.order.OrderQueryFragment;
import com.tianhe.pay.ui.payment.bankcard.BankcardPayFragment;
import com.tianhe.pay.ui.payment.cash.CashPayFragment;
import com.tianhe.pay.ui.payment.coupon.CouponPayFragment;
import com.tianhe.pay.ui.payment.pos.PosPayFragment;
import com.tianhe.pay.ui.payment.storedvalue.StoredValueCardPayFragment;
import com.tianhe.pay.ui.payment.wechatali.WechatAliPayFragment;
import com.tianhe.pay.ui.refund.RefundOrderFragment;
import com.tianhe.pay.ui.refund.bankcard.BankcardRefundPayFragment;
import com.tianhe.pay.ui.refund.cash.CashRefundPayFragment;
import com.tianhe.pay.ui.refund.coupon.CouponRefundPayFragment;
import com.tianhe.pay.ui.refund.pos.PosRefundPayFragment;
import com.tianhe.pay.ui.refund.storedvalue.StoredValueCardRefundPayFragment;
import com.tianhe.pay.ui.refund.wechatali.WechatAliRefundPayFragment;
import com.tianhe.pay.ui.setting.SettingFragment;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQueryFragment;
import com.tianhe.pay.ui.wechatalireprint.WechatAliQuerySelectorFragment;
import com.tianhe.pay.ui.wechatalireprint.WechatAliReprintFragment;
import com.tianhe.pay.ui.welcome.WelcomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @PerFragment
    @ContributesAndroidInjector(modules = WelcomeModule.class)
    abstract WelcomeFragment welcomeFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginFragment loginFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeFragment homeFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CheckoutModule.class)
    abstract CheckoutFragment checkoutFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PayModule.class)
    abstract WechatAliPayFragment wechatAliPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CashPayFragment cashPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CouponPayFragment couponPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract StoredValueCardPayFragment storedValueCardPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract BankcardPayFragment bankcardPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract PosPayFragment posPayFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CrmModule.Member.class)
    abstract QueryMemberFragment queryMemberFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CrmModule.StoredValueCard.class)
    abstract QueryStoredValueCardFragment queryStoredValueCardFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CrmModule.Coupon.class)
    abstract QueryCouponFragment queryCouponFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CartFragment cartFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CartModule.class)
    abstract CartDetailFragment cartDetailFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = OrderHistoryModule.class)
    abstract OrderHistoryFragment orderHistoryFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = OrderQueryModule.class)
    abstract OrderQueryFragment orderQueryFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = OrderHistoryDetailModule.class)
    abstract OrderHistoryDetailFragment orderHistoryDetailFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = RefundOrderModule.class)
    abstract RefundOrderFragment refundOrderFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = RefundPaidModule.class)
    abstract WechatAliRefundPayFragment wechatAliRefundFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CashRefundPayFragment cashRefundFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CouponRefundPayFragment couponRefundFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract StoredValueCardRefundPayFragment storedValueCardRefundFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract BankcardRefundPayFragment bankcardRefundFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract PosRefundPayFragment posRefundPayFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract SettingFragment settingFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = AuthModule.class)
    abstract QueryAuthFragment queryAuthFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = TodayCountModule.class)
    abstract OrderCountFragment orderTodayCountFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = WechatAliReprint.class)
    abstract WechatAliQueryFragment wechatAliQueryFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract WechatAliQuerySelectorFragment wechatAliQuerySelectorFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract WechatAliReprintFragment wechatAliReprintFragment();
}
