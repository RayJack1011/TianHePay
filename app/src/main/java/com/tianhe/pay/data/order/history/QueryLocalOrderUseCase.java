package com.tianhe.pay.data.order.history;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.db.DbCache;
import com.tianhe.pay.data.UseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

public class QueryLocalOrderUseCase extends UseCase<QueryLocalCommand> {
    DbCache dbCache;
    QueryLocalCommand request;

    @Inject
    public QueryLocalOrderUseCase(@MainThread Scheduler mainScheduler,
                                  @RpcThread Scheduler rpcScheduler,
                                  DbCache dbCache) {
        super(mainScheduler, rpcScheduler);
        this.dbCache = dbCache;
    }

    @Override
    protected Observable<List<Order>> buildUseCaseObservable() {
        return Observable.create(new ObservableOnSubscribe<List<Order>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Order>> e) throws Exception {
                List<Order> orders = dbCache.queryOrderHistory(request);
                e.onNext(orders);
            }
        });
    }

    @Override
    public void setReqParam(QueryLocalCommand reqParam) {
        this.request = reqParam;
    }
}
