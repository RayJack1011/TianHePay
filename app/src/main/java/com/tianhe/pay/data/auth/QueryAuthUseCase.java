package com.tianhe.pay.data.auth;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryAuthUseCase extends UseCase<QueryAuth> {

    Repository repository;
    QueryAuth queryAuth;

    @Inject
    public QueryAuthUseCase(@MainThread Scheduler mainScheduler,
                            @RpcThread Scheduler rpcScheduler,
                            Repository repository) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.queryAuth(queryAuth);
    }

    @Override
    public void setReqParam(QueryAuth reqParam) {
        this.queryAuth = reqParam;
    }
}
