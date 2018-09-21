package com.tianhe.pay.data.wechatali.ali;

import com.ali.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FQueryResult;
import com.ali.demo.trade.service.AlipayTradeService;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

public class AliQueryTask extends UseCase<AlipayTradeQueryRequestBuilder> {

    AlipayTradeService service;
    AlipayTradeQueryRequestBuilder request;

    @Inject
    public AliQueryTask(@MainThread Scheduler mainScheduler,
                        @RpcThread Scheduler rpcScheduler,
                        AlipayTradeService service) {
        super(mainScheduler, rpcScheduler);
        this.service = service;
    }

    @Override
    protected Observable<AlipayF2FQueryResult> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<AlipayTradeQueryRequestBuilder, AlipayF2FQueryResult>() {
                    @Override
                    public AlipayF2FQueryResult apply(AlipayTradeQueryRequestBuilder queryRequest) throws Exception {
                        return service.queryTradeResult(queryRequest);
                    }
                });
    }

    @Override
    public void setReqParam(AlipayTradeQueryRequestBuilder reqParam) {
        this.request = reqParam;
    }
}
