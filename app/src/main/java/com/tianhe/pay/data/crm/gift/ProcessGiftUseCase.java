package com.tianhe.pay.data.crm.gift;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.CrmApi;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ProcessGiftUseCase extends UseCase<ProcessGift> {

    CrmApi crmApi;
    ProcessGift request;


    @Inject
    public ProcessGiftUseCase(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              CrmApi crmApi) {
        super(mainScheduler, rpcScheduler);
        this.crmApi = crmApi;

    }

    @Override
    protected Observable buildUseCaseObservable() {
        return crmApi.submitGift(request);
    }

    @Override
    public void setReqParam(ProcessGift reqParam) {
        this.request = reqParam;
    }
}
