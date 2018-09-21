package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_protocol.RefundResData;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class TencentRefundTask extends UseCase<RefundReqData> {

    TencentApi api;
    RefundReqData request;

    @Inject
    public TencentRefundTask(@MainThread Scheduler mainScheduler,
                             @RpcThread Scheduler rpcScheduler,
                             TencentApi api) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
    }

    @Override
    protected Observable<RefundResData> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<RefundReqData, String>() {
                    @Override
                    public String apply(RefundReqData reqData) throws Exception {
                        return Util.objectToXml(reqData);
                    }
                }).flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String requestXml) throws Exception {
                        return api.invokeTencetApi(Configure.REFUND_API, requestXml);
                    }
                }).map(new Function<ResponseBody, RefundResData>() {
                    @Override
                    public RefundResData apply(ResponseBody responseBody) throws Exception {
                        String responseXml = responseBody.string();
                        return (RefundResData) Util.getObjectFromXML(responseXml, RefundResData.class);
                    }
                });
    }

    @Override
    public void setReqParam(RefundReqData reqParam) {
        this.request = reqParam;
    }
}
