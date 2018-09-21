package com.tianhe.pay.data.login;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.Repository;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.db.DbCache;
import com.tianhe.pay.data.UseCase;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

public class UserLogin extends UseCase<LoginReq> {
    private transient Repository repository;
    private DbCache dbCache;
    private LoginReq req;

    @Inject
    public UserLogin(@MainThread Scheduler mainScheduler,
                     @RpcThread Scheduler rpcScheduler,
                     Repository repository, DbCache dbCache) {
        super(mainScheduler, rpcScheduler);
        this.repository = repository;
        this.dbCache = dbCache;
    }

    @Override
    public void setReqParam(LoginReq req) {
        this.req = req;
    }

    @Override
    protected Observable<LoginResp> buildUseCaseObservable() {
        return repository.login(req)
                .doOnNext(new Consumer<LoginResp>() {
                    @Override
                    public void accept(LoginResp loginResp) throws Exception {
                        dbCache.reduceOrders(new Date());
                    }
                });
    }

}
