package com.tianhe.pay.data.order.calculate;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class CalculateOrderUseCase extends UseCase<Order> {
    private Repository repository;
    private Order order;

    @Inject
    public CalculateOrderUseCase(@MainThread Scheduler mainScheduler,
                                 @RpcThread Scheduler rpcScheduler,
                                 Repository repository) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
    }

    @Override
    public void setReqParam(Order order) {
        this.order = order;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.calculateOrder(order);
    }

}
