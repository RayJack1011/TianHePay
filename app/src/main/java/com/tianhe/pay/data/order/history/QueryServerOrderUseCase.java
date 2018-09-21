package com.tianhe.pay.data.order.history;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryServerOrderUseCase extends UseCase<String> {

    String saleNo;
    Repository repository;

    @Inject
    public QueryServerOrderUseCase(@MainThread Scheduler mainScheduler,
                                   @RpcThread Scheduler rpcScheduler,
                                   Repository repository) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.queryHistoryOrder(saleNo);
    }

    @Override
    public void setReqParam(String reqParam) {
        this.saleNo = reqParam;
    }
}
