package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.app.DownloadAppUseCase;
import com.tianhe.pay.data.app.GetAppUpgradeUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.welcome.WelcomeContract;
import com.tianhe.pay.ui.welcome.WelcomePresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class WelcomeModule {

    @PerFragment
    @Binds
    abstract WelcomeContract.Presenter welcomePresenter(WelcomePresenter presenter);

    @Provides
    @PerFragment
    @Named("appLast")
    static UseCase providerGetAppLastTask(GetAppUpgradeUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("downloadApp")
    static UseCase providerDownloadAppTask(DownloadAppUseCase task) {
        return task;
    }

}
