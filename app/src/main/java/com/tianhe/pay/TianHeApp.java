package com.tianhe.pay;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.di.components.AppComponent;
import com.tianhe.pay.di.components.DaggerAppComponent;
import com.tianhe.pay.utils.ActivityLifecycleCallbacksAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.OkHttpClient;

public class TianHeApp extends Application implements HasActivityInjector,
        HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    private List<Activity> allActivities = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        CrashHandlers crashHandler = CrashHandlers.getInstance();
        crashHandler.init(getApplicationContext());
        setupLeakCanary();
        setupStetho();
//        setupCrashHandler();
        registerActivityListener();
        initializeInjector();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    private void initializeInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
    }

    protected void setupStetho() {
        Stetho.initializeWithDefaults(this);
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        enabledStrictMode();
        return LeakCanary.install(this);
    }

    private void setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                allActivities.add(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                allActivities.remove(activity);
            }
        });
    }

    private void enabledStrictMode() {
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
//                    .detectAll()    // 侦测一切潜在违规
//                    .penaltyLog()   // 违规时打印log
//                    .penaltyDeath() // 违规时直接崩溃
//                    .build());
//        }
    }

    public void exitApp() {
        int size = allActivities.size();
        for (int i = size - 1; i > 0; i--) {
            Activity activity = allActivities.remove(i);
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
