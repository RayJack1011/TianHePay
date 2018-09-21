package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.history.QueryLocalOrderUseCase;
import com.tianhe.pay.data.order.history.SyncOrdersUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.order.OrderHistoryPresenter;
import com.tianhe.pay.ui.order.OrderHistoryContract;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OrderHistoryModule {

    @PerFragment
    @Binds
    abstract OrderHistoryContract.Presenter localHistoryPresenter(OrderHistoryPresenter presenter);

    @Provides
    @PerFragment
    @Named("queryLocalHistory")
    static UseCase providerLocalHistoryTask(QueryLocalOrderUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("syncTodayOrders")
    static UseCase providerSyncTodayOrdersTask(SyncOrdersUseCase task) {
        return task;
    }
}
