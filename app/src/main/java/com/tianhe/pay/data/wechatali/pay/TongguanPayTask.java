package com.tianhe.pay.data.wechatali.pay;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.wechatali.TongguanApi;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TongguanPayTask extends UseCase<TongguanPayRequest> {
    TongguanApi api;
    TongguanPayRequest request;
    Sign md5Sign;

    @Inject
    public TongguanPayTask(@MainThread Scheduler mainScheduler,
                           @RpcThread Scheduler rpcScheduler,
                           TongguanApi api, @Named("md5Sign") Sign md5Sign) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
        this.md5Sign = md5Sign;
    }

    @Override
    public void setReqParam(TongguanPayRequest request) {
        this.request = request;
    }

    @Override
    protected Observable<TongguanPayResponse> buildUseCaseObservable() {
        request.signed(md5Sign);
        return api.pay(request);
    }
}
