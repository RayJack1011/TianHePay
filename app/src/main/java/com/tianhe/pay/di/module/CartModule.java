package com.tianhe.pay.di.module;

import com.tianhe.pay.data.order.calculate.CalculateOrderUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class CartModule {

    @Provides
    @PerFragment
    @Named("calculateModify")
    static UseCase providerCalculateTask(CalculateOrderUseCase task) {
        return task;
    }
}
