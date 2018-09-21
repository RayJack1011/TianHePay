package com.tianhe.pay.data.wechatali.ali;

import com.ali.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FRefundResult;
import com.ali.demo.trade.service.AlipayTradeService;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

public class AliRefundTask extends UseCase<AlipayTradeRefundRequestBuilder> {

    AlipayTradeService service;
    AlipayTradeRefundRequestBuilder request;

    @Inject
    public AliRefundTask(@MainThread Scheduler mainScheduler,
                         @RpcThread Scheduler rpcScheduler,
                         AlipayTradeService service) {
        super(mainScheduler, rpcScheduler);
        this.service = service;
    }

    @Override
    protected Observable<AlipayF2FRefundResult> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<AlipayTradeRefundRequestBuilder, AlipayF2FRefundResult>() {
                    @Override
                    public AlipayF2FRefundResult apply(AlipayTradeRefundRequestBuilder refundRequest) throws Exception {
                        return service.tradeRefund(refundRequest);
                    }
                });
    }

    @Override
    public void setReqParam(AlipayTradeRefundRequestBuilder reqParam) {
        this.request = reqParam;
    }
}
