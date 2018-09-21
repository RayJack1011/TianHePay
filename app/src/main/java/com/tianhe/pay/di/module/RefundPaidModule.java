package com.tianhe.pay.di.module;

import com.tianhe.pay.data.wechatali.ali.AliQueryTask;
import com.tianhe.pay.data.wechatali.ali.AliRefundTask;
import com.tianhe.pay.data.wechatali.query.TongguanQueryTask;
import com.tianhe.pay.data.wechatali.refund.TongguanRefundTask;
import com.tianhe.pay.data.wechatali.tencent.TencentQueryTask;
import com.tianhe.pay.data.wechatali.tencent.TencentRefundTask;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RefundPaidModule {

    @Provides
    @PerFragment
    @Named("wechatAliRefund")
    static UseCase providerWechatAliRefund(TongguanRefundTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("wechatAliQuery")
    static UseCase providerWechatAliQuery(TongguanQueryTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("tencentRefund")
    static UseCase providerTencentRefund(TencentRefundTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("tencentQuery")
    static UseCase providerTencentQuery(TencentQueryTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("aliRefund")
    static UseCase providerAliRefund(AliRefundTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("aliQuery")
    static UseCase providerAliQuery(AliQueryTask task) {
        return task;
    }

}
