package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 微信原生的撤销
 */
public class TencentReverseTask extends UseCase<ReverseReqData> {
    TencentApi api;
    ReverseReqData request;

    @Inject
    public TencentReverseTask(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              TencentApi api) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
    }

    @Override
    protected Observable<ReverseResData> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<ReverseReqData, String>() {
                    @Override
                    public String apply(ReverseReqData reqData) throws Exception {
                        return Util.objectToXml(reqData);
                    }
                }).flatMap(new Function<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(String requestXml) throws Exception {
                        return api.invokeTencetApi(Configure.REVERSE_API, requestXml);
                    }
                }).map(new Function<ResponseBody, ReverseResData>() {
                    @Override
                    public ReverseResData apply(ResponseBody responseBody) throws Exception {
                        String responseXml = responseBody.string();
                        return (ReverseResData) Util.getObjectFromXML(responseXml, ReverseResData.class);
                    }
                });
    }

    @Override
    public void setReqParam(ReverseReqData reqParam) {
        this.request = reqParam;
    }
}
