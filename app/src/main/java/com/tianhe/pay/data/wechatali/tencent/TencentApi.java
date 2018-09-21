package com.tianhe.pay.data.wechatali.tencent;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 微信原生的API
 */
public interface TencentApi {
    @POST
    Observable<ResponseBody> invokeTencetApi(@Url String url, @Body String xml);

}
