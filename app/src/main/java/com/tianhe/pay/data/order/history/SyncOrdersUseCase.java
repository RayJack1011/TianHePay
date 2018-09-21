package com.tianhe.pay.data.order.history;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.db.DbCache;
import com.tianhe.pay.model.Global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class SyncOrdersUseCase extends UseCase<SyncCommand> {

    DbCache dbCache;
    Repository repository;
    SyncCommand command;

    Global global;

    @Inject
    public SyncOrdersUseCase(@MainThread Scheduler mainScheduler,
                             @RpcThread Scheduler rpcScheduler,
                             Repository repository,
                             DbCache dbCache,
                             Global global) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
        this.dbCache = dbCache;
        this.global = global;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.queryOrderSimplesByDate(command).map(new Function<List<OrderSimple>, List<Order>>() {
            @Override
            public List<Order> apply(@NonNull List<OrderSimple> orderSimples) throws Exception {
                dbCache.reduceOrders(new Date());
                List<Order> localOrders = dbCache.queryOrderHistory(
                        new QueryLocalCommand(new Date(command.getTime()), command.getIsPractice()));
                if (orderSimples == null || orderSimples.size() == 0) {
                    return localOrders;
                }


                List<String> syncSaleNos = new ArrayList<String>();
                for (OrderSimple orderSimple : orderSimples) {
                    String syncSaleNo = orderSimple.getSaleNo();
                    boolean needSync = true;
                    for (Order localOrder : localOrders) {
                        if (localOrder.getOrderHeader().getSaleNo().equals(syncSaleNo)) {
                            needSync = false;
                            break;
                        }
                    }
                    if (needSync) {
                        syncSaleNos.add(syncSaleNo);
                    }
                }
                if (syncSaleNos.size() == 0) {
                    return localOrders;
                }
                for (String saleNo : syncSaleNos) {//----->getBill接口
                    Order order = repository.queryHistoryOrder(saleNo).blockingFirst();
                    if (order != null) {
                        dbCache.saveOrder(order);
                        localOrders.add(order);
                    }
                }
                return localOrders;
            }
        });
    }

    @Override
    public void setReqParam(SyncCommand reqParam) {
        this.command = reqParam;
    }

}
