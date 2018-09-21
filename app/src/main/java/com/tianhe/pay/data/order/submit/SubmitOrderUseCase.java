package com.tianhe.pay.data.order.submit;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.db.DbCache;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

public class SubmitOrderUseCase extends UseCase<Order> {
    Repository repository;
    Order order;
    DbCache dbCache;

    @Inject
    public SubmitOrderUseCase(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              Repository repository, DbCache dbCache) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
        this.dbCache = dbCache;
    }

    @Override
    protected Observable<SubmitOrderResult> buildUseCaseObservable() {
        return repository.submitOrder(order).doOnNext(new Consumer<SubmitOrderResult>() {
            @Override
            public void accept(SubmitOrderResult result) throws Exception {
                order.getOrderHeader().setTime(result.getOpTime());
                dbCache.saveOrder(order);
            }
        });
    }

    @Override
    public void setReqParam(Order reqParam) {
        this.order = reqParam;
    }
}
