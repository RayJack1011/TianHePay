package com.tianhe.pay.di.module;

import com.tianhe.pay.data.order.calculate.CalculateOrderUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.home.HomeContract;
import com.tianhe.pay.ui.home.HomePresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HomeModule {
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract HomeFragment homeFragment();

    @PerFragment
    @Binds
    abstract HomeContract.Presenter homePresenter(HomePresenter presenter);

    @Provides
    @PerFragment
    @Named("calculateKeypad")
    static UseCase providerCalculateTask(CalculateOrderUseCase task) {
        return task;
    }

}
