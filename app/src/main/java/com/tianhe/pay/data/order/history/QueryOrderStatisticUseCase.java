package com.tianhe.pay.data.order.history;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.order.OrderStatisticsPay;
import com.tianhe.pay.db.DbCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryOrderStatisticUseCase extends UseCase<SyncCommand> {
    private Repository repository;
//    DbCache dbCache;

    SyncCommand command;

    @Inject
    public QueryOrderStatisticUseCase(@MainThread Scheduler mainScheduler,
                                      @RpcThread Scheduler rpcScheduler,
                                      Repository repository,
                                      DbCache dbCache) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
//        this.dbCache = dbCache;
    }

    @Override
    protected Observable<OrderStatistics> buildUseCaseObservable() {
        return repository.countByDate(command);
    }

    @Override
    public void setReqParam(SyncCommand reqParam) {
        this.command = reqParam;
    }
}
