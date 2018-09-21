package com.tianhe.pay.data.wechatali.ali;

import com.ali.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FPayResult;
import com.ali.demo.trade.service.AlipayTradeService;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

public class AliPayTask extends UseCase<AlipayTradePayRequestBuilder> {
    AlipayTradeService service;
    AlipayTradePayRequestBuilder request;

    @Inject
    public AliPayTask(@MainThread Scheduler mainScheduler,
                      @RpcThread Scheduler rpcScheduler,
                      AlipayTradeService service) {
        super(mainScheduler, rpcScheduler);
        this.service = service;
    }

    @Override
    protected Observable<AlipayF2FPayResult> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<AlipayTradePayRequestBuilder, AlipayF2FPayResult>() {
                    @Override
                    public AlipayF2FPayResult apply(AlipayTradePayRequestBuilder payRequest) throws Exception {
                        return service.tradePay(payRequest);
                    }
                });
    }

    @Override
    public void setReqParam(AlipayTradePayRequestBuilder reqParam) {
        this.request = reqParam;
    }

}
