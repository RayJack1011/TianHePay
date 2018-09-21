package com.tianhe.pay.di.module;

import android.content.Context;

import com.tianhe.devices.CardReader;
import com.tianhe.devices.Printer;
import com.tianhe.devices.n900.N900CardReader;
import com.tianhe.devices.n900.N900Printer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DeviceModule {
    @Singleton
    @Provides
    CardReader provideCardReader(Context context) {
        return new N900CardReader(context);
    }

    @Singleton
    @Provides
    Printer providePrinter(Context context) {
        return new N900Printer(context);
    }
}
