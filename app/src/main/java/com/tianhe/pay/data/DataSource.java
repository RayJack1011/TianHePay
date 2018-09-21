package com.tianhe.pay.data;

import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.order.history.OrderHistory;
import com.tianhe.pay.data.order.history.OrderSimple;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.data.login.LoginResp;
import com.tianhe.pay.data.app.AppUpgrade;
import com.tianhe.pay.ui.modify.ModifyResp;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DataSource {
    String SCHEME = "http://";
    String HOST_AND_PORT = "113.105.185.131:8110";
//    String HOST_AND_PORT = "172.29.36.12:8081";
    String LAST_APP = "/apk/appversion";
    String LOGIN = "/tianHePayService/service/login";
    String MODIFY = "/tianHePayService/service/changePassWd";
    String CALCULAT_ORDER = "/tianHePayService/service/getGoodsCalculation";
    String LAST_SALE_NO = "/tianHePayService/service/getSaleNo";
    String SUBMIT_BILL = "/tianHePayService/service/billSale";
    String QUERY_BILL_HISTORY = "/tianHePayService/service/getBill";
    String QUERY_BILL_LIST = "/tianHePayService/service/getBillList";
    String COUNT_PAY = "/tianHePayService/service/getPayCount";
    String CRM = "/tianHePayService/service/cardService";
    String AUTH = "/tianHePayService/service/getAuthCardList";
    String WX_REFUND = "/tianHePayService/service/wxRefund";
    String ALI_REFUND = "/tianHePayService/service/alipayRefund";
    String ALI_QUARY = "/tianHePayService/service/alipayQuery";

    @GET
    Observable<AppUpgrade> lastApp(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String downloadUrl);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<ModifyResp>> modify(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<LoginResp>> login(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<CalculationResult>> calculateOrder(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<String>> getLastOrderNo(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<SubmitOrderResult>> submitOrder(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<OrderHistory>> queryHistoryOrder(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<List<OrderSimple>>> queryOrderSimples(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<OrderStatistics>> countOrder(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ServerResult<Auth>> queryAuth(@Url String url, @Field("sign") String sign, @Field("data") String data);

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> crmAdapter(@Url String url, @Field("xml") String xml);
}
