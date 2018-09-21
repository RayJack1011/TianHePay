package com.tianhe.pay.data.wechatali.query;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.wechatali.TongguanApi;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TongguanQueryTask extends UseCase<TongguanQueryRequest> {
    Sign md5Sign;
    TongguanApi api;
    TongguanQueryRequest request;

    @Inject
    public TongguanQueryTask(@MainThread Scheduler mainScheduler,
                             @RpcThread Scheduler rpcScheduler,
                             TongguanApi api, @Named("md5Sign") Sign md5Sign) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
        this.md5Sign = md5Sign;
    }

    @Override
    public void setReqParam(TongguanQueryRequest request) {
        this.request = request;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        request.signed(md5Sign);
        return api.query(request);
    }


}
