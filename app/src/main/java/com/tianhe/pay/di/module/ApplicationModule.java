package com.tianhe.pay.di.module;

import android.app.Application;
import android.content.Context;

import com.tianhe.pay.BuildConfig;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.db.DatabaseHelper;
import com.tianhe.pay.db.DbCache;
import com.tianhe.pay.db.DbCacheImpl;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.ui.Nav;
import com.tianhe.pay.ui.RealRes;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.DeviceInfos;
import com.tianhe.pay.utils.Res;
import com.tianhe.pay.utils.ToastFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Provides @Singleton @MainThread
    static Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides @Singleton @RpcThread
    static Scheduler provideRpcScheduler() {
        return Schedulers.io();
    }

    @Provides @Singleton
    static Res provideRes(Context context) {
        return new RealRes(context);
    }

    @Provides @Singleton
    static ToastFactory provideToastFactory(Context context) {
        return new ToastFactory.Default(context);
    }

    @Provides @Singleton @Named("currentVersion")
    static String provideCurrentVersion(Context context) {
        return DeviceInfos.currentVersion(context);
    }

    @Provides @Singleton @Named("wifiMac")
    static String provideWifiMac(Context context) {
        if (BuildConfig.DEBUG) {
//            return "34873d113a18";
//            return "34873d1139f2";
            return DeviceInfos.getWifiMac(context);
        }
        return DeviceInfos.getWifiMac(context);
    }

    @Provides @Singleton
    static Global provideGlobal() {
        return new Global();
    }

    @Provides @Singleton
    static CartManager provideCartManager(Global global, @Named("currentVersion") String version) {
        return new CartManager(global, version);
    }

    @Provides @Singleton
    static RefundDataManager provideRefundManager(Global global, @Named("currentVersion") String version) {
        return new RefundDataManager(global, version);
    }

    @Provides @Singleton
    static Nav provideNav(Context context) {
        return new Nav(context);
    }

    @Provides @Singleton
    static Settings provideSettings(Context context, Global global) {
        return new Settings(context, global);
    }

    @Provides @Singleton
    static DatabaseHelper provideDbHelper(Context context) {
        return new DatabaseHelper(context);
    }

    @Provides @Singleton
    static DbCache provideDbCache(DatabaseHelper databaseHelper) {
        return new DbCacheImpl(databaseHelper);
    }
}
