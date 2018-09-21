package com.tianhe.pay.data.wechatali.tencent;

import android.util.Log;

import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class TencentPayTask extends UseCase<ScanPayReqData> {
    TencentApi api;
    ScanPayReqData request;

    @Inject
    public TencentPayTask(@MainThread Scheduler mainScheduler,
                          @RpcThread Scheduler rpcScheduler,
                          TencentApi api) {
        super(mainScheduler, rpcScheduler);
        this.api = api;
    }

    @Override
    protected Observable<ScanPayResData> buildUseCaseObservable() {
        return Observable.just(request)
                .map(new Function<ScanPayReqData, String>() {
                    @Override
                    public String apply(ScanPayReqData reqData) throws Exception {
                        return Util.objectToXml(reqData);
                    }
                }).flatMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(String requestXml) throws Exception {
                        return api.invokeTencetApi(Configure.PAY_API, requestXml);
                    }
                }).map(new Function<ResponseBody, ScanPayResData>() {
                    @Override
                    public ScanPayResData apply(ResponseBody responseBody) throws Exception {
                        String response = responseBody.string();
                        ScanPayResData data = (ScanPayResData) Util.getObjectFromXML(response, ScanPayResData.class);
                        return data;
                    }
                });
    }

    @Override
    public void setReqParam(ScanPayReqData reqParam) {
        this.request = reqParam;
    }
}
