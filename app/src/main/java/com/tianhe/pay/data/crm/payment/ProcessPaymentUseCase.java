package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.crm.CrmApi;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class ProcessPaymentUseCase extends UseCase<ProcessPayment> {
    CrmApi crmApi;
    ProcessPayment request;

    @Inject
    public ProcessPaymentUseCase(@MainThread Scheduler mainScheduler,
                                 @RpcThread Scheduler rpcScheduler,
                                 CrmApi crmApi) {
        super(mainScheduler, rpcScheduler);
        this.crmApi = crmApi;
    }

    @Override
    protected Observable<ProcessPaymentResult> buildUseCaseObservable() {
        return crmApi.submitPayment(request);
    }

    @Override
    public void setReqParam(ProcessPayment reqParam) {
        this.request = reqParam;
    }
}
