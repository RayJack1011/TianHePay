package com.tianhe.pay.di.components;

import android.app.Application;

import com.tianhe.pay.TianHeApp;
import com.tianhe.pay.di.module.ActivityModule;
import com.tianhe.pay.di.module.ApplicationModule;
import com.tianhe.pay.di.module.DeviceModule;
import com.tianhe.pay.di.module.FragmentModule;
import com.tianhe.pay.di.module.NetModule;
import com.tianhe.pay.di.module.ApiModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {NetModule.class, ApiModule.class, ApplicationModule.class,
        DeviceModule.class,
        ActivityModule.class, FragmentModule.class, AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<TianHeApp> {

    void inject(TianHeApp app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
