package com.tianhe.pay.data.wechatali.ali;

import com.ali.demo.api.response.AlipayTradeCancelResponse;
import com.ali.demo.trade.model.builder.AlipayTradeCancelRequestBuilder;
import com.ali.demo.trade.service.AlipayTradeService;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

public class AliCancelTask extends UseCase<AlipayTradeCancelRequestBuilder> {

    AlipayTradeService service;
    AlipayTradeCancelRequestBuilder request;

    @Inject
    public AliCancelTask(@MainThread Scheduler mainScheduler,
                         @RpcThread Scheduler rpcScheduler,
                         AlipayTradeService service) {
        super(mainScheduler, rpcScheduler);
        this.service = service;
    }

    @Override
    protected Observable<AlipayTradeCancelResponse> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<AlipayTradeCancelRequestBuilder, AlipayTradeCancelResponse>() {
                    @Override
                    public AlipayTradeCancelResponse apply(AlipayTradeCancelRequestBuilder
                                                                   cancelRequest) throws Exception {
                        return service.tradeCancel(cancelRequest);
                    }
                });
    }

    @Override
    public void setReqParam(AlipayTradeCancelRequestBuilder reqParam) {
        this.request = reqParam;
    }
}
