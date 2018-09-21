package com.tianhe.pay.ui.payment.wechatali;

import android.text.TextUtils;
import android.util.Log;

import com.ali.demo.api.response.AlipayTradeCancelResponse;
import com.ali.demo.trade.Constants;
import com.ali.demo.trade.model.GoodsDetail;
import com.ali.demo.trade.model.TradeStatus;
import com.ali.demo.trade.model.builder.AlipayTradeCancelRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FPayResult;
import com.ali.demo.trade.model.result.AlipayF2FQueryResult;
import com.google.gson.Gson;
import com.tianhe.devices.Printer;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.Md5Sign;
import com.tianhe.pay.data.TianheSign;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

public class AliPayPresenter extends TianHePresenter<WechatAliPayContract.View> implements WechatAliPayContract
        .Presenter {
    private static final int QUERY_COUNT_MAX = 20;

    protected Global global;
    protected CartManager cartManager;

    Settings settings;
    UseCase aliPayTask;
    UseCase aliQueryTask;
    UseCase aliCancelTask;
    PrintUseCase printTask;
    AtomicInteger queryCount = new AtomicInteger(0);
    transient boolean cancelQuery;
    Payment inFlightPayment;
    String reverseReason = null;

    @Inject
    public AliPayPresenter(Global global, CartManager cartManager, Settings settings,
                           @Named("aliPay") UseCase aliPayTask,
                           @Named("aliQuery") UseCase aliQueryTask,
                           @Named("aliCancel") UseCase aliCancelTask,
                           PrintUseCase printTask) {
        this.global = global;
        this.cartManager = cartManager;
        this.settings = settings;
        this.aliPayTask = aliPayTask;
        this.aliQueryTask = aliQueryTask;
        this.aliCancelTask = aliCancelTask;
        this.printTask = printTask;
    }

    @Override
    public void pay(Payment payment, Money payAmount, String code) {
        inFlightPayment = payment;
        currentSaleNoUsed();
        cancelQuery = false;
        initAlipay(payAmount, code);
//        AlipayTradePayRequestBuilder request = createPayRequest(payAmount, code);
//        aliPayTask.setReqParam(request);
//        aliPayTask.execute(payDisposable);
    }

    /**
     * 支付宝支付
     */
    private void initAlipay(Money payAmount, String code) {
        String subject = global.getSupplierName() + "当面付消费";
        Map<String, String> map = new HashMap<>();
        CommomData.addSaleNo(cartManager.getSafeSaleNo());
        map.put("outTradeNo", global.getShopNo() + cartManager.getSafeSaleNo());
        map.put("scene", "bar_code");
        map.put("authCode", code);
        map.put("subject", subject);
        map.put("storeId", global.getShopNo());
        map.put("totalAmount", payAmount.toString());
        String data = new Gson().toJson(map);
        Log.e("qqq", "gson 转json ---->" + data);
        Log.e("qqq", "MD5加密----->" + new Md5Sign().sign(data + "4ea29e6d44854ded8f1271a20cb1e85b"));
        OkHttpUtils.post()
                .url("http://45.40.244.160:8080/alipay/tradePay?" +
                        "bizData=" + data +
                        "&sign=" + new Md5Sign().sign(data + "4ea29e6d44854ded8f1271a20cb1e85b") +
                        "&machId=3")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("qqq", "切支付宝失败回调----->" + e.getMessage());
                        notifyPayFail("支付宝支付失败!!!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("qqq", "切支付宝成功回调 ----->" + response);
                        try {
                            if (!TextUtils.isEmpty(response)) {
                                JSONObject jsonObject = new JSONObject(response);
                                String payCode = jsonObject.getString("code");//支付返回码
                                String subCodes = jsonObject.getString("subCode");//请求成功码
                                Log.e("qqq", "返回数据 ---->" + subCodes);
                                if (!"null".equals(subCodes)) {//请求支付宝服务器失败
                                    if (TextUtils.isEmpty(jsonObject.getString("subMsg"))) {
                                        notifyPayFail("请求支付宝服务器失败");
                                    } else {
                                        notifyPayFail(jsonObject.getString("subMsg"));
                                    }
                                } else {//请求支付宝服务器成功
                                    if (Constants.SUCCESS.equals(payCode)) {//付款成功
                                        String total = jsonObject.getString("totalAmount");
                                        notifyPaySuccess(Money.createAsYuan(total), jsonObject.getString("outTradeNo"));
                                    } else if (Constants.PAYING.equals(payCode)) {//用户付款中
                                        tryWechatAliQuery();
                                    } else {
                                        notifyPayFail(" 支付失败");
                                    }
                                }
                            } else {
                                notifyPayFail(" 支付失败");
                            }
                        } catch (JSONException e) {
                            notifyPayFail(" 支付失败");
                            e.printStackTrace();
                        }
                    }
                });
    }

    private AlipayTradePayRequestBuilder createPayRequest(Money payAmount, String code) {
//        String subject = "xxx品牌xxx门店当面付消费";
        String subject = global.getSupplierName() + "当面付消费";
//
//
//        // 支付宝创建
//        data =
//                "{\"outTradeNo\":\"201503200101010026\"," +
//                        "\"scene\":\"bar_code\"," +
//                        "\"authCode\":\"282760560409399457\"," +
//                        "\"subject\":\"test\"," +
//                        "\"totalAmount\":\"0.01\"}";
//        connectionUrl("http://45.40.244.160:8080/alipay/tradePay",
//                        "bizData=" +data +
//                        "&sign=" + EncryptUtil.MD5(data +"4ea29e6d44854ded8f1271a20cb1e85b") +
//                        "&machId=3");
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem item : cartManager.getCartItems()) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(item.getBarcode(), item.getName(),
                    item.getPrice().getAmount(), item.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        AlipayTradePayRequestBuilder builder = new AlipayTradePayRequestBuilder()
                .setScene("bar_code")
                .setOutTradeNo(global.getShopNo() + cartManager.getSafeSaleNo())
                .setSubject(subject)
                .setAuthCode(code)
                .setTotalAmount(payAmount.toString())
                .setStoreId(global.getShopNo())
//                .setBody(body)
                .setOperatorId(global.getUserNo())
                .setGoodsDetailList(goodsDetailList)
                .setTimeoutExpress("5m");
        return builder;
    }

    @Override
    public void cancelWaiting() {
        this.cancelQuery = true;
        if (!payDisposable.isDisposed()) {
            payDisposable.dispose();
        }
        if (!queryDisposable.isDisposed()) {
            queryDisposable.dispose();
        }
        skipSaleNo();
    }

    @Override
    public void print(final PaidInfo paidInfo) {
        Log.e("alipay", "pay success, and start print");
        WechatAliOrder wechatAliOrder = new WechatAliOrder();
        wechatAliOrder.setDatetime(paidInfo.getTime());
        wechatAliOrder.setPayName(paidInfo.getPaymentName());
        wechatAliOrder.setLowOrderId(global.getShopNo() + cartManager.getSafeSaleNo());
        wechatAliOrder.setUpOrderId(paidInfo.getBillNo());
        wechatAliOrder.setOrderState("消费");
        wechatAliOrder.setShopNo(global.getShopNo());
        wechatAliOrder.setUserNo(global.getUserNo());
        wechatAliOrder.setTerminalNo(global.getTerminalId());
        wechatAliOrder.setPayMoney(paidInfo.getSaleAmount());

        printTask.setReqParam(PrintUtils.wechatAliPrint(wechatAliOrder));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                Log.e("print", state.toString());
                if (state != Printer.State.NORMAL) {
                    view.printFail(state.getMessage(),paidInfo);
                } else {
                    view.printSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.printFail(e.getMessage());
            }
        });
    }

    private void notifyPaySuccess(Money money, String relationNumber) {
        currentSaleNoUsed();

        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(inFlightPayment.getPaymentId());
        paidInfo.setSaleAmount(money);
        paidInfo.setBillNo(relationNumber);
        paidInfo.setPaymentName(inFlightPayment.getPaymentName());
        paidInfo.setTime(Times.nowDate());
        cartManager.addPaidInfo(paidInfo);
        settings.saveWechatAli(cartManager.getSafeSaleNo());
        view.paySuccess(paidInfo);
    }

    private void notifyPayFail(String reason) {
        skipSaleNo();
        view.payFailed(reason);
    }

    private void skipSaleNo() {
        String newSn = settings.skipLocalSerialNumber();
        String saleNoPrefix = global.getTerminalId() + Times.yyMMddHHmmss(new Date());
        String newSaleNo = saleNoPrefix + newSn;
        cartManager.setSafeSaleNo(newSaleNo);
    }

    // 微信/支付宝支付过后订单需要保存, 避免下次支付时失败
    private void currentSaleNoUsed() {
        settings.saveUsedSaleNo(cartManager.getSafeSaleNo());
    }

    private DefaultObserver payDisposable = new DefaultObserver<AlipayF2FPayResult>() {
        @Override
        public void onNext(AlipayF2FPayResult result) {
            onWechatAliPayReturn(result);
            dispose();
        }

        @Override
        public void onError(Throwable e) {
            notifyPayFail(e.getMessage());
        }
    };

    private void onWechatAliPayReturn(AlipayF2FPayResult result) {
        Log.e("AlipayF2FPayResult", new Gson().toJson(result));

        TradeStatus tradeStatus = result.getTradeStatus();
        switch (tradeStatus) {
            case SUCCESS:
                Log.e("qqq", "支付宝支付成功的回参----->" + new Gson().toJson(result));
                notifyPaySuccess(Money.createAsYuan(result.getResponse().getTotalAmount()),
                        result.getResponse().getOutTradeNo());
                break;
            case WAITING:
                tryWechatAliQuery();
                break;
            case UNKNOWN:
                wechatAliQuery();
                break;
            case FAILED:
                notifyPayFail("支付宝支付失败!!!");
                break;
            default:
                notifyPayFail("支付失败: 不支持的交易状态，交易返回异常!!!");
                break;
        }
    }

    private void tryWechatAliQuery() {
        if (cancelQuery) {
            return;
        }
        if (queryCount.getAndIncrement() > QUERY_COUNT_MAX) {
            cancelQuery = true;
            // TODO: 暂时没有自动冲正
            notifyPayFail("支付失败");
            queryCount.set(0);
        } else {
//            view.waitingCustomerPay();
            view.waitingForPay("正在进行第" + queryCount.getAndIncrement() + " 次查询");
            Observable.timer(5000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    wechatAliQuery();
                }
            });
        }
    }

    /**
     * 查询支付宝
     */
    private void initQuery() {
//        view.waitingForPay("");
        try {
            JSONObject js = new JSONObject();
            js.put("outTradeNo", global.getShopNo() + cartManager.getSafeSaleNo());
            OkHttpUtils.post()
                    .addParams("sign", new TianheSign().sign(js.toString()))
                    .addParams("data", js.toString())
                    .url(DataSource.SCHEME + DataSource.HOST_AND_PORT + DataSource.ALI_QUARY)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
//                            notifyPayFail(e.getMessage());
//                            view.
//                            queryCount.set(0);
                            view.waitingForPay("链接超时，正在重试，请等待。。。");
                            tryWechatAliQuery();
                        }


                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("qqq", "支付宝查询回参----->" + response);
                            try {
                                JSONObject json = new JSONObject(response);
                                if ("1".equals(json.getString("code"))) {//等待用户支付
                                    if ("WAIT_BUYER_PAY".equals(json.getString("msg"))) {
                                        view.waitingForPay("查询结果:用户付款中，请等待。。。");
                                        tryWechatAliQuery();
                                    } else {
                                        view.showMessage(json.getString("msg"));
                                    }
                                } else if ("0".equals(json.getString("code"))) {
                                    String jsons = json.getString("data");
                                    JSONObject jss = new JSONObject(jsons);
                                    String total = jss.getString("totalAmount");
                                    notifyPaySuccess(Money.createAsYuan(total),
                                            jss.getString("outTradeNo"));
                                } else {
//                                    view.waitingForPay("用户付款中，请等待。。。");
                                    notifyPayFail(json.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        .addParams("sign", new TianheSign().sign(js.toString()))
//                .addParams("data", js.toString())

    }

    private DefaultObserver<AlipayF2FQueryResult> queryDisposable = new DefaultObserver<AlipayF2FQueryResult>() {
        @Override
        public void onNext(AlipayF2FQueryResult result) {
            onWechatAliQueryReturn(result);
        }

        @Override
        public void onError(Throwable e) {
            notifyPayFail(e.getMessage());
            queryCount.set(0);
        }
    };

    private void onWechatAliQueryReturn(AlipayF2FQueryResult result) {
        TradeStatus tradeStatus = result.getTradeStatus();
        switch (tradeStatus) {
            case SUCCESS:
                notifyPaySuccess(
                        Money.createAsYuan(result.getResponse().getTotalAmount()),
                        result.getResponse().getTradeNo());
                break;
            case WAITING:
                // 等待用户付款, 继续查询
                tryWechatAliQuery();
            case UNKNOWN:
                if (isLoopQuery()) {
                    tryWechatAliQuery();
                } else {
                    notifyPayFail("支付宝接口异常");
                    // 天和百货怕出现单边账后无法追回款项, 注释掉, 如果买家付款后有争议, 直接查微信/支付宝账单
//                    doCancelPay("支付宝接口异常");
                }
                break;
            case FAILED:
                notifyPayFail("支付失败");
                queryCount.set(0);
                break;
        }
    }

    private void wechatAliQuery() {
        AlipayTradeQueryRequestBuilder builder =
                new AlipayTradeQueryRequestBuilder()
                        .setOutTradeNo(cartManager.getSafeSaleNo());
        initQuery();
//        aliQueryTask.setReqParam(builder);
//        aliQueryTask.execute(queryDisposable);
    }

    private boolean isLoopQuery() {
        return queryCount.get() > 0;
    }

    private void doCancelPay(String reverseReason) {
        this.reverseReason = reverseReason;
        AlipayTradeCancelRequestBuilder request = new AlipayTradeCancelRequestBuilder()
                .setOutTradeNo(cartManager.getSafeSaleNo());
        aliCancelTask.setReqParam(request);
        aliCancelTask.execute(cancelDisposable);
    }

    private DefaultObserver<AlipayTradeCancelResponse> cancelDisposable = new DefaultObserver<AlipayTradeCancelResponse>() {

        @Override
        public void onNext(AlipayTradeCancelResponse response) {
            if (response.isSuccess()) {
                if (reverseReason != null) {
                    notifyPayFail("支付失败: " + reverseReason);
                } else {
                    notifyPayFail("支付失败, 已撤销订单. 如已扣款项, 将原路退回");
                }
            } else {
                // 撤销订单失败, 产生的单边账需要人工处理
                notifyPayFail("支付失败, 请联系管理人员");
            }
            reverseReason = null;
        }

        @Override
        public void onError(Throwable e) {
            reverseReason = null;
            notifyPayFail(e.getMessage());
            queryCount.set(0);
        }
    };
}
