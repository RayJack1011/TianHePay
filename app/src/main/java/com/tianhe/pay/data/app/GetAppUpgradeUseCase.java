package com.tianhe.pay.data.app;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetAppUpgradeUseCase extends UseCase<Void> {

    private Repository repository;

    @Inject
    public GetAppUpgradeUseCase(@MainThread Scheduler mainScheduler,
                                @RpcThread Scheduler rpcScheduler,
                                Repository repository) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
    }

    @Override
    public void setReqParam(Void reqParam) {

    }

    @Override
    protected Observable<AppUpgrade> buildUseCaseObservable() {
        return repository.lastApp();
    }
}
