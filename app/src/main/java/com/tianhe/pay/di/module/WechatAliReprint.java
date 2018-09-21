package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.wechatali.ali.AliQueryTask;
import com.tianhe.pay.data.wechatali.query.TongguanQueryTask;
import com.tianhe.pay.data.wechatali.tencent.TencentQueryTask;
import com.tianhe.pay.di.PerFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class WechatAliReprint {

    @Provides
    @PerFragment
    @Named("wechatAliQuery")
    static UseCase providerWechatAliQuery(TongguanQueryTask task) {
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
    @Named("aliQuery")
    static UseCase providerAliQuery(AliQueryTask task) {
        return task;
    }

}
