package com.tianhe.pay.di.module;

import com.tianhe.pay.data.crm.payment.ProcessPaymentUseCase;
import com.tianhe.pay.data.order.calculate.CalculateOrderUseCase;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoUseCase;
import com.tianhe.pay.data.order.submit.SubmitOrderUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.checkout.CheckoutContract;
import com.tianhe.pay.ui.checkout.CheckoutPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CheckoutModule {
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract CheckoutFragment checkoutFragment();

    @PerFragment
    @Binds
    abstract CheckoutContract.Presenter checkoutPresenter(CheckoutPresenter presenter);

    @Provides
    @PerFragment
    @Named("calculate")
    static UseCase providerCalculateTask(CalculateOrderUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("maxSaleNo")
    static UseCase providerGetLastSaleNoTask(GetLastSaleNoUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("submitOrder")
    static UseCase providerSubmitBillTask(SubmitOrderUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("processPayment")
    static UseCase providerProcessPaymentTask(ProcessPaymentUseCase task) {
        return task;
    }

}
