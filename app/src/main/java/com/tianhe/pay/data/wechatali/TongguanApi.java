package com.tianhe.pay.data.wechatali;

import com.tianhe.pay.data.wechatali.pay.TongguanPayRequest;
import com.tianhe.pay.data.wechatali.pay.TongguanPayResponse;
import com.tianhe.pay.data.wechatali.query.TongguanQueryRequest;
import com.tianhe.pay.data.wechatali.query.TongguanQueryResponse;
import com.tianhe.pay.data.wechatali.refund.TongguanRefundRequest;
import com.tianhe.pay.data.wechatali.refund.TongguanRefundResponse;
import com.tianhe.pay.data.wechatali.reverse.TongguanReverseRequest;
import com.tianhe.pay.data.wechatali.reverse.TongguanReverseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TongguanApi {

    @POST(TongguanConstant.URL_PAY)
    Observable<TongguanPayResponse> pay(@Body TongguanPayRequest request);

    @POST(TongguanConstant.URL_QUERY)
    Observable<TongguanQueryResponse> query(@Body TongguanQueryRequest request);

    @POST(TongguanConstant.URL_REVERSE)
    Observable<TongguanReverseResponse> reverse(@Body TongguanReverseRequest request);

    @POST(TongguanConstant.URL_REFUND)
    Observable<TongguanRefundResponse> refund(@Body TongguanRefundRequest request);
}
