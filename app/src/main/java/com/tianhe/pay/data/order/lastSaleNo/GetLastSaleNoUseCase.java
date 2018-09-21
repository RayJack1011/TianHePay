package com.tianhe.pay.data.order.lastSaleNo;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetLastSaleNoUseCase extends UseCase<GetLastSaleNoRequest> {

    Repository repository;
    GetLastSaleNoRequest request;

    @Inject
    public GetLastSaleNoUseCase(@MainThread Scheduler mainScheduler,
                                @RpcThread Scheduler rpcScheduler,
                                Repository repository) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<String> buildUseCaseObservable() {
        return repository.getLastSaleNo(request);
    }

    @Override
    public void setReqParam(GetLastSaleNoRequest reqParam) {
        this.request = reqParam;
    }
}
