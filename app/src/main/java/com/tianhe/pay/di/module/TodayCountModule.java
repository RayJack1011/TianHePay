package com.tianhe.pay.di.module;

import com.tianhe.pay.data.order.history.QueryOrderStatisticUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.order.OrderCountContract;
import com.tianhe.pay.ui.order.OrderCountPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TodayCountModule {

    @PerFragment
    @Binds
    abstract OrderCountContract.Presenter refundOrderPresenter(OrderCountPresenter presenter);

    @Provides
    @PerFragment
    @Named("queryStatistic")
    static UseCase providerWechatAliReverse(QueryOrderStatisticUseCase task) {
        return task;
    }

}
