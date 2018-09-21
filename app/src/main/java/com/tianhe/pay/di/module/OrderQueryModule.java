package com.tianhe.pay.di.module;

import com.tianhe.pay.data.order.history.QueryServerOrderUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.order.OrderQueryContract;
import com.tianhe.pay.ui.order.OrderQueryPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OrderQueryModule {

    @PerFragment
    @Binds
    abstract OrderQueryContract.Presenter localHistoryPresenter(OrderQueryPresenter presenter);

    @Provides
    @PerFragment
    @Named("queryServerHistory")
    static UseCase providerLocalHistoryTask(QueryServerOrderUseCase task) {
        return task;
    }

}
