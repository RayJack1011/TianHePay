package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.payment.ProcessPaymentUseCase;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoUseCase;
import com.tianhe.pay.data.order.submit.SubmitOrderUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.refund.RefundOrderContract;
import com.tianhe.pay.ui.refund.RefundOrderPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RefundOrderModule {

    @PerFragment
    @Binds
    abstract RefundOrderContract.Presenter refundOrderPresenter(RefundOrderPresenter presenter);


    @Provides
    @PerFragment
    @Named("refundOrder")
    static UseCase providerSubmitBillTask(SubmitOrderUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("querySaleNo")
    static UseCase providerGetLastSaleNoTask(GetLastSaleNoUseCase task) {
        return task;
    }

    @Provides
    @PerFragment
    @Named("processPayment")
    static UseCase providerProcessPaymentTask(ProcessPaymentUseCase task) {
        return task;
    }

}
