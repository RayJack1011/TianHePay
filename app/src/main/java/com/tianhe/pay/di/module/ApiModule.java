package com.tianhe.pay.di.module;

import android.content.Context;

import com.ali.demo.Configs;
import com.ali.demo.trade.service.AlipayTradeService;
import com.ali.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.google.gson.Gson;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.TianHeRepositoryImpl;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.crm.CrmApi;
import com.tianhe.pay.data.crm.ForwardCrmApiImpl;
import com.tianhe.pay.data.wechatali.TongguanApi;
import com.tianhe.pay.data.wechatali.tencent.TencentApi;
import com.tianhe.pay.ui.setting.Settings;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import zlc.season.rxdownload2.RxDownload;

@Module
public class ApiModule {

    @Provides @Singleton
    public DataSource provideDataSource(Retrofit retrofit) {
        return retrofit.create(DataSource.class);
    }

    @Provides @Singleton
    public Repository provideRepository(DataSource dataSource,
                                        @Named("md5Sign")Sign md5Sign,
                                        @Named("tianheSign") Sign tianheSign, Gson gson,
                                        Settings settings) {
        return new TianHeRepositoryImpl(dataSource, md5Sign, tianheSign, gson, settings);
    }

    @Provides @Singleton
    public TongguanApi provideTongguanApi(Retrofit retrofit) {
        return retrofit.create(TongguanApi.class);
    }

    @Provides @Singleton
    public CrmApi provideCrmApi(DataSource dataSource, @Named("md5Sign")Sign md5Sign, Settings settings) {
        return new ForwardCrmApiImpl(md5Sign, dataSource, settings);
//        return new CrmApiImpl(md5Sign);
    }

    @Provides @Singleton
    public TencentApi provideTencentApi(Retrofit retrofit) {
        return retrofit.create(TencentApi.class);
    }

    @Provides @Singleton
    public AlipayTradeService provideAlipayTradeService() {
        Configs.init();
        return new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Provides
    @Singleton
    static RxDownload provideDownloader(Context context, Retrofit retrofit) {
        return RxDownload.getInstance(context).retrofit(retrofit).maxThread(1).maxRetryCount(0);
    }
}
