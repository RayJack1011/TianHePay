package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.wechatali.ali.AliCancelTask;
import com.tianhe.pay.data.wechatali.ali.AliPayTask;
import com.tianhe.pay.data.wechatali.ali.AliQueryTask;
import com.tianhe.pay.data.wechatali.pay.TongguanPayTask;
import com.tianhe.pay.data.wechatali.query.TongguanQueryTask;
import com.tianhe.pay.data.wechatali.reverse.TongguanReverseTask;
import com.tianhe.pay.data.wechatali.tencent.TencentPayTask;
import com.tianhe.pay.data.wechatali.tencent.TencentQueryTask;
import com.tianhe.pay.data.wechatali.tencent.TencentReverseTask;
import com.tianhe.pay.di.PerFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class PayModule {

    @Provides
    @PerFragment
    @Named("wechatAliPay")
    static UseCase providerWechatAliPay(TongguanPayTask task) {
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
    @Named("wechatAliReverse")
    static UseCase providerWechatAliReverse(TongguanReverseTask task) {
        return task;
    }


    @Provides
    @PerFragment
    @Named("tencentPay")
    static UseCase providerTencentRay(TencentPayTask task) {
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
    @Named("tencentReverse")
    static UseCase providerTencentReverse(TencentReverseTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("aliPay")
    static UseCase providerAliPay(AliPayTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("aliQuery")
    static UseCase providerAliQuery(AliQueryTask task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("aliCancel")
    static UseCase providerAliCancel(AliCancelTask task) {
        return task;
    }
}
