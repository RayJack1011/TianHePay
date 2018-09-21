package com.tianhe.pay.di.module;

import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.order.OrderHistoryDetailContract;
import com.tianhe.pay.ui.order.OrderHistoryDetailPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class OrderHistoryDetailModule {

    @PerFragment
    @Binds
    abstract OrderHistoryDetailContract.Presenter refundOrderPresenter(OrderHistoryDetailPresenter presenter);

}
