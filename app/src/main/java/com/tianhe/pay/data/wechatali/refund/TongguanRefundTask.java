package com.tianhe.pay.data.wechatali.refund;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.wechatali.TongguanApi;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class TongguanRefundTask extends UseCase<TongguanRefundRequest> {
    Sign md5Sign;
    TongguanApi api;
    TongguanRefundRequest request;

    @Inject
    public TongguanRefundTask(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              TongguanApi api, @Named("md5Sign") Sign md5Sign) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
        this.md5Sign = md5Sign;
    }

    @Override
    public void setReqParam(TongguanRefundRequest request) {
        this.request = request;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        request.signed(md5Sign);
        return api.refund(request);
    }

}
