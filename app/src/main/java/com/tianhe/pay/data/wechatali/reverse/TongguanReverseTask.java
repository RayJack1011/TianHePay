package com.tianhe.pay.data.wechatali.reverse;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.wechatali.TongguanApi;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TongguanReverseTask extends UseCase<TongguanReverseRequest> {

    TongguanApi api;
    Sign md5Sign;
    TongguanReverseRequest request;

    @Inject
    public TongguanReverseTask(@MainThread Scheduler mainScheduler,
                               @RpcThread Scheduler rpcScheduler,
                               TongguanApi api, @Named("md5Sign") Sign md5Sign) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
        this.md5Sign = md5Sign;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        request.signed(md5Sign);
        return api.reverse(request);
    }

    @Override
    public void setReqParam(TongguanReverseRequest request) {
        this.request = request;
    }
}
