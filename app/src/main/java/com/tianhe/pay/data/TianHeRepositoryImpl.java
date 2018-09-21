package com.tianhe.pay.data;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.auth.QueryAuth;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.order.calculate.PaymentLimit;
import com.tianhe.pay.data.order.history.OrderHistory;
import com.tianhe.pay.data.order.history.OrderHistoryPaid;
import com.tianhe.pay.data.order.history.OrderMapper;
import com.tianhe.pay.data.order.history.QueryServerOrder;
import com.tianhe.pay.data.order.history.OrderSimple;
import com.tianhe.pay.data.order.history.SyncCommand;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.data.login.LoginReq;
import com.tianhe.pay.data.login.LoginResp;
import com.tianhe.pay.data.app.AppUpgrade;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoRequest;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.modify.ModifyReq;
import com.tianhe.pay.ui.modify.ModifyResp;
import com.tianhe.pay.ui.setting.Settings;

import java.io.File;
import java.util.List;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

import static com.tianhe.pay.data.DataSource.AUTH;
import static com.tianhe.pay.data.DataSource.CALCULAT_ORDER;
import static com.tianhe.pay.data.DataSource.COUNT_PAY;
import static com.tianhe.pay.data.DataSource.LAST_APP;
import static com.tianhe.pay.data.DataSource.LAST_SALE_NO;
import static com.tianhe.pay.data.DataSource.LOGIN;
import static com.tianhe.pay.data.DataSource.MODIFY;
import static com.tianhe.pay.data.DataSource.QUERY_BILL_HISTORY;
import static com.tianhe.pay.data.DataSource.QUERY_BILL_LIST;
import static com.tianhe.pay.data.DataSource.SCHEME;
import static com.tianhe.pay.data.DataSource.SUBMIT_BILL;

public class TianHeRepositoryImpl implements Repository {
    DataSource dataSource;
    //    Sign sign;
    Sign md5Sign;
    Sign tianheSign;
    Gson gson;
    Settings settings;

    public TianHeRepositoryImpl(DataSource dataSource,
                                @Named("md5Sign") Sign md5Sign,
                                @Named("tianheSign") Sign tianheSign,
                                Gson gson,
                                Settings settings) {
        this.dataSource = dataSource;
        this.md5Sign = md5Sign;
        this.tianheSign = tianheSign;
        this.gson = gson;
        this.settings = settings;
}

    @Override
    public Observable<AppUpgrade> lastApp() {
        String hostAndPort = settings.getServerUrl();
        if (hostAndPort == null) {
            hostAndPort = DataSource.HOST_AND_PORT;
        }
        String url = SCHEME + hostAndPort + LAST_APP;
        return dataSource.lastApp(url);
    }

    @Override
    public Observable<File> downloadApp(String downloadUrl, final ProgressResponseBody.ProgressListener listener) {
        return dataSource.download(downloadUrl)
                .flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(@NonNull ResponseBody responseBody) throws Exception {
                        File downloadDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS)
                                .getAbsoluteFile();
                        final File file = new File(downloadDir, "Tianhe.apk");
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(responseBody.source());
                        sink.flush();
                        sink.close();
                        return Observable.just(file);
                    }
                });
    }

//    /**
//     * 修改密码
//     * @param modifyReq
//     * @return
//     */
//    @Override
//    public Observable<ModifyResp> modify(ModifyReq modifyReq) {
//        return Observable.just(modifyReq)
//                .map(new Function<ModifyReq, String[]>() {
//                    @Override
//                    public String[] apply(ModifyReq loginReq) throws Exception {
//                        loginReq.oldPasswd = md5Sign.sign(loginReq.userNo+loginReq.oldPasswd);
//                        loginReq.newPasswd = md5Sign.sign(loginReq.userNo+loginReq.newPasswd);
//                        String json = gson.toJson(loginReq);
//                        return new String[]{tianheSign.sign(json), json};
//                    }
//                }).flatMap(new Function<String[], ObservableSource<ServerResult<ModifyResp>>>() {
//                    @Override
//                    public ObservableSource<ServerResult<ModifyResp>> apply(String[] req) throws Exception {
//                        String hostAndPort = settings.getServerUrl();
//                        if (hostAndPort == null) {
//                            hostAndPort = DataSource.HOST_AND_PORT;
//                        }
//                        String url = SCHEME + hostAndPort + MODIFY;
//                        return dataSource.modify(url, req[0], req[1]);
//                    }
//                }).map(new Function<ServerResult<ModifyResp>, ModifyResp>() {
//                    @Override
//                    public ModifyResp apply(@NonNull ServerResult<ModifyResp> result) throws Exception {
//                        if (result.isSuccess()) {
//                            Log.e("qqq",new Gson().toJson(result.getData())+"：：：：登陆返回数据");
//                            return result.getData();
//                        }
//                        throw new RuntimeException(result.getMsg());
//                    }
//                });
//    }


    @Override
    public Observable<LoginResp> login(final LoginReq request) {
        return Observable.just(request)
                .map(new Function<LoginReq, String[]>() {
                    @Override
                    public String[] apply(LoginReq loginReq) throws Exception {
                        loginReq.passwd = md5Sign.sign(loginReq.userNo+loginReq.passwd);
                        String json = gson.toJson(loginReq);
                        return new String[]{tianheSign.sign(json), json};
                    }
                }).flatMap(new Function<String[], ObservableSource<ServerResult<LoginResp>>>() {
                    @Override
                    public ObservableSource<ServerResult<LoginResp>> apply(String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + LOGIN;
                        return dataSource.login(url, req[0], req[1]);
                    }
                }).map(new Function<ServerResult<LoginResp>, LoginResp>() {
                    @Override
                    public LoginResp apply(@NonNull ServerResult<LoginResp> result) throws Exception {
                        if (result.isSuccess()) {
                            Log.e("qqq","登陆接口回参----->"+new Gson().toJson(result.getData()));
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }



    @Override
    public Observable<CalculationResult> calculateOrder(Order order) {
        return Observable.just(order)
                .map(new Function<Order, String[]>() {
                    @Override
                    public String[] apply(Order order) throws Exception {
                        String json = gson.toJson(order);
                        return new String[]{tianheSign.sign(json), json};
                    }
                })
                .flatMap(new Function<String[], ObservableSource<ServerResult<CalculationResult>>>() {
                    @Override
                    public ObservableSource<ServerResult<CalculationResult>> apply(String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + CALCULAT_ORDER;
                        return dataSource.calculateOrder(url, req[0], req[1]);
                    }
                })
                .map(new Function<ServerResult<CalculationResult>, CalculationResult>() {
                    @Override
                    public CalculationResult apply(@NonNull ServerResult<CalculationResult> result) throws
                            Exception {
                        if (result.isSuccess()) {
                            List<PaymentLimit> limitCoupon = result.getData().getLimitCoupon();
                            CommomData.limitCoupon.addAll(limitCoupon);
                            Log.e("qqq",result.getData().getCouponList()+":::::::::;;;返回数据");
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<String> getLastSaleNo(GetLastSaleNoRequest request) {
        return Observable.just(request)
                .map(new Function<GetLastSaleNoRequest, String[]>() {
                    @Override
                    public String[] apply(GetLastSaleNoRequest req) throws Exception {
                        String json = gson.toJson(req);
                        return new String[]{tianheSign.sign(json), json};
                    }
                })
                .flatMap(new Function<String[], ObservableSource<ServerResult<String>>>() {
                    @Override
                    public ObservableSource<ServerResult<String>> apply(String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + LAST_SALE_NO;
                        return dataSource.getLastOrderNo(url, req[0], req[1]);
                    }
                })
                .map(new Function<ServerResult<String>, String>() {
                    @Override
                    public String apply(@NonNull ServerResult<String> result) throws
                            Exception {
                        if (result.isSuccess()) {
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<SubmitOrderResult> submitOrder(Order order) {
        return Observable.just(order)
                .map(new Function<Order, String[]>() {
                    @Override
                    public String[] apply(Order order) throws Exception {
                        String json = gson.toJson(order);
                        Log.e("qqq","billSale--入参--->"+json);
                        return new String[]{tianheSign.sign(json), json};
                    }
                })
                .flatMap(new Function<String[], ObservableSource<ServerResult<SubmitOrderResult>>>() {
                    @Override
                    public ObservableSource<ServerResult<SubmitOrderResult>> apply(String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + SUBMIT_BILL;
                        return dataSource.submitOrder(url, req[0], req[1]);
                    }
                })
                .map(new Function<ServerResult<SubmitOrderResult>, SubmitOrderResult>() {
                    @Override
                    public SubmitOrderResult apply(@NonNull ServerResult<SubmitOrderResult> result) throws
                            Exception {
                        Log.e("qqq","billSale回参----------------->"+new Gson().toJson(result));
                        if (result.isSuccess()) {
                            Log.e("qqq","------------->提交订单成功");
                            return result.getData();
                        }
                        Log.e("qqq","------------->提交订单失败");
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<Order> queryHistoryOrder(String saleNo) {
        return Observable.just(saleNo)
                .map(new Function<String, String[]>() {
                    @Override
                    public String[] apply(String saleNo) throws Exception {
                        QueryServerOrder query = new QueryServerOrder();
                        query.setSaleNo(saleNo);
                        query.setShopNo(CommomData.shopNos);
//                        query.setShopNo("1002");
                        Log.e("qqq",new Gson().toJson(query)+":::::::::::订单查询入参");
                        String json = gson.toJson(query);
                        return new String[]{tianheSign.sign(json), json};
                    }
                })
                .flatMap(new Function<String[], ObservableSource<ServerResult<OrderHistory>>>() {
                    @Override
                    public ObservableSource<ServerResult<OrderHistory>> apply(String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + QUERY_BILL_HISTORY;
                        return dataSource.queryHistoryOrder(url, req[0], req[1]);
                    }
                })
                .map(new Function<ServerResult<OrderHistory>, Order>() {
                    @Override
                    public Order apply(@NonNull ServerResult<OrderHistory> result) throws
                            Exception {
                        Log.e("qqq",new Gson().toJson(result)+"--------->流水查询回参");
                        if (result.isSuccess()) {
                            for(OrderHistoryPaid entity:result.getData().getPaids()){
                                if (entity.getPaymentId().equals("8005")) {
                                    entity.setPaymentId("03");
                                }
                                if (entity.getPaymentId().equals("4001")) {//券
                                    entity.setPaymentId("01");
                                }
                                if (entity.getPaymentId().equals("8007")) {
                                    entity.setPaymentId("02");
                                }
                                if (entity.getPaymentId().equals("5006")) {
                                    entity.setPaymentId("04");
                                }
                                if (entity.getPaymentId().equals("9004")) {
                                    entity.setPaymentId("07");
                                }
                                if (entity.getPaymentId().equals("80070")) {
                                    entity.setPaymentId("05");
                                }
                                if (entity.getPaymentId().equals("5099")) {
                                    entity.setPaymentId("06");
                                }

                                //add by hujie 2018-05-29 ----start
                                if (entity.getPaymentId().equals("8098")) {
                                    entity.setPaymentId("05");
                                }
                                if (entity.getPaymentId().equals("5065")) {
                                    entity.setPaymentId("06");
                                }
                                if (entity.getPaymentId().equals("8099")) {
                                    entity.setPaymentId("07");
                                }
                                //add by hujie 2018-05-29 ----end

                            }
                            return OrderMapper.mapper(result.getData());
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<OrderStatistics> countByDate(SyncCommand command) {

        return Observable.just(command)
                .map(new Function<SyncCommand, String[]>() {
                    @Override
                    public String[] apply(@NonNull SyncCommand command) throws Exception {
                        String json = gson.toJson(command);
                        return new String[]{tianheSign.sign(json), json};
                    }
                }).flatMap(new Function<String[], ObservableSource<ServerResult<OrderStatistics>>>() {
                    @Override
                    public ObservableSource<ServerResult<OrderStatistics>> apply(@NonNull String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + COUNT_PAY;

                        return dataSource.countOrder(url, req[0], req[1]);
                    }
                }).map(new Function<ServerResult<OrderStatistics>, OrderStatistics>() {
                    @Override
                    public OrderStatistics apply(ServerResult<OrderStatistics> result) throws
                            Exception {
                        if (result.isSuccess()) {
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<List<OrderSimple>> queryOrderSimplesByDate(SyncCommand command) {
        return Observable.just(command)
                .map(new Function<SyncCommand, String[]>() {
                    @Override
                    public String[] apply(@NonNull SyncCommand command) throws Exception {
                        String json = gson.toJson(command);
                        return new String[]{tianheSign.sign(json), json};
                    }
                }).flatMap(new Function<String[], ObservableSource<ServerResult<List<OrderSimple>>>>() {
                    @Override
                    public ObservableSource<ServerResult<List<OrderSimple>>> apply(@NonNull String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + QUERY_BILL_LIST;

                        return dataSource.queryOrderSimples(url, req[0], req[1]);
                    }
                }).map(new Function<ServerResult<List<OrderSimple>>, List<OrderSimple>>() {
                    @Override
                    public List<OrderSimple> apply(@NonNull ServerResult<List<OrderSimple>> result) throws Exception {
                        if (result.isSuccess()) {
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }

    @Override
    public Observable<Auth> queryAuth(QueryAuth queryAuth) {
        return Observable.just(queryAuth)
                .map(new Function<QueryAuth, String[]>() {
                    @Override
                    public String[] apply(@NonNull QueryAuth queryAuth) throws Exception {
                        String json = gson.toJson(queryAuth);
                        return new String[]{tianheSign.sign(json), json};
                    }
                }).flatMap(new Function<String[], ObservableSource<ServerResult<Auth>>>() {
                    @Override
                    public ObservableSource<ServerResult<Auth>> apply(@NonNull String[] req) throws Exception {
                        String hostAndPort = settings.getServerUrl();
                        if (hostAndPort == null) {
                            hostAndPort = DataSource.HOST_AND_PORT;
                        }
                        String url = SCHEME + hostAndPort + AUTH;
                        return dataSource.queryAuth(url, req[0], req[1]);
                    }
                }).map(new Function<ServerResult<Auth>, Auth>() {
                    @Override
                    public Auth apply(@NonNull ServerResult<Auth> result) throws Exception {
                        if (result.isSuccess()) {
                            return result.getData();
                        }
                        throw new RuntimeException(result.getMsg());
                    }
                });
    }
}
