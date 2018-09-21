package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class TencentQueryTask extends UseCase<ScanPayQueryReqData> {
    ScanPayQueryReqData request;
    TencentApi api;

    @Inject
    public TencentQueryTask(@MainThread Scheduler mainScheduler,
                            @RpcThread Scheduler rpcScheduler,
                            TencentApi api) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
    }

    @Override
    protected Observable<ScanPayQueryResData> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<ScanPayQueryReqData, String>() {
                    @Override
                    public String apply(ScanPayQueryReqData reqData) throws Exception {
                        return Util.objectToXml(reqData);
                    }
                }).flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String requestXml) throws Exception {
                        return api.invokeTencetApi(Configure.PAY_QUERY_API, requestXml);
                    }
                }).map(new Function<ResponseBody, ScanPayQueryResData>() {
                    @Override
                    public ScanPayQueryResData apply(ResponseBody responseBody) throws Exception {
                        String responseXml = responseBody.string();
                        return (ScanPayQueryResData) Util.getObjectFromXML(responseXml, ScanPayQueryResData.class);
                    }
                });
    }

    @Override
    public void setReqParam(ScanPayQueryReqData reqParam) {
        this.request = reqParam;
    }
}
