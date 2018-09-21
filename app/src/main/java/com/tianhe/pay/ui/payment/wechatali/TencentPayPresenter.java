package com.tianhe.pay.ui.payment.wechatali;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tianhe.devices.Printer;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
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

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Call;

public class TencentPayPresenter extends TianHePresenter<WechatAliPayContract.View> implements WechatAliPayContract.Presenter {
    private static final int QUERY_COUNT_MAX = 10;

    protected Global global;
    protected CartManager cartManager;

    Settings settings;
    UseCase tencentPayTask;
    UseCase tencentQueryTask;
    UseCase tencentReverseTask;
    PrintUseCase printTask;
    AtomicInteger queryCount = new AtomicInteger(0);
    transient boolean cancelQuery;

    Payment inFlightPayment;
    String reverseReason = null;

    @Inject
    public TencentPayPresenter(Global global, CartManager cartManager, Settings settings,
                               @Named("tencentPay") UseCase tencentPayTask,
                               @Named("tencentQuery") UseCase tencentQueryTask,
                               @Named("tencentReverse") UseCase tencentReverseTask,
                               PrintUseCase printTask) {
        this.global = global;
        this.cartManager = cartManager;
        this.settings = settings;
        this.tencentPayTask = tencentPayTask;
        this.tencentQueryTask = tencentQueryTask;
        this.tencentReverseTask = tencentReverseTask;
        this.printTask = printTask;
    }

    @Override
    public void pay(Payment payment, Money payAmount, String code) {
        inFlightPayment = payment;
        currentSaleNoUsed();
        cancelQuery = false;
        ScanPayReqData request = createPayRequest(payAmount, code);
        Log.e("qqq","微信入参----->"+new Gson().toJson(request));
        initPay(request);
//        tencentPayTask.setReqParam(request);
//        tencentPayTask.execute(payDisposable);
    }

    /**
     * 微信支付（换框架）
     */
    private void initPay(ScanPayReqData request) {
        String requestxml=Util.objectToXml(request);
        OkHttpUtils
                .postString()
                .content(requestxml)
                .url(Configure.PAY_API)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("qqq","微信支付接口回参（失败）----->"+e.getMessage());
//                        onPayFail(e.getMessage());
//                        view.waitingQuary();
//                        view.payFailed("请求超时，查询中（是否支付）");】
                        view.showMessage("支付超时，查询中");
                        tryWechatAliQuery();
//                        wechatAliQuery();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("qqq","微信支付接口回参（成功）----->"+response);
                        ScanPayResData objectFromXML =(ScanPayResData) Util.getObjectFromXML(response, ScanPayResData.class);
                        onWechatAliPayReturn(objectFromXML);
                    }
                });

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

    private void skipSaleNo() {
        String newSn = settings.skipLocalSerialNumber();
        String saleNoPrefix = global.getTerminalId() + Times.yyMMddHHmmss(new Date());
        String newSaleNo = saleNoPrefix + newSn;
        cartManager.setSafeSaleNo(newSaleNo);
    }

    @Override
    public void print(final PaidInfo paidInfo) {
        WechatAliOrder wechatAliOrder = new WechatAliOrder();
        wechatAliOrder.setDatetime(paidInfo.getTime());
        wechatAliOrder.setPayName(paidInfo.getPaymentName());
        wechatAliOrder.setLowOrderId(global.getShopNo()+cartManager.getSafeSaleNo());
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
                if (state != Printer.State.NORMAL) {
                    view.printFail(state.getMessage(),paidInfo);
//                    view.printFail(state.getMessage());
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

    @Override
    public void onDestroy() {
        payDisposable.dispose();
    }

    private ScanPayReqData createPayRequest(Money payAmount, String code) {
        ScanPayReqData.Builder builder = ScanPayReqData.newBuilder();
        CommomData.addSaleNo(cartManager.getSafeSaleNo());//单边时产生列表流水号
        builder.setBizParams(global.getShopNo()+cartManager.getSafeSaleNo(), (int) payAmount.getAmount(), code,
                cartManager.getSafeSaleNo(), Times.yyyyMMddHHmmss(new Date()));
        builder.setDeviceParams(global.getTerminalId(), "172.29.0.1");
        return builder.build();
    }

    private void onWechatAliPayReturn(ScanPayResData response) {
        Log.e("qqq",new Gson().toJson(response)+"：：：：微信支付回参");
        if (response.isSuccess()) {
            if (response.isTransactionSuccess()) {
//                view.showMessage("支付成功");
                currentSaleNoUsed();
                onPaySuccess(Money.createAsFen(response.getTotal_fee()), response.getOut_trade_no());
                return;
            }
            // "等待用户付款"
            if (response.isWaitingCustomer()) {
//                view.showMessage("用户输入密码中");
                tryWechatAliQuery();
                return;
            }
            // 天和百货怕出现单边账后无法追回款项, 注释掉, 如果买家付款后有争议, 直接查微信/支付宝账单
            // 授权码无效/失效、用于余额不足, 需要执行撤销接口
//            if (response.needReverse()) {
//                doReverse(response.getErr_code_des());
//                return;
//            }
            //这里是获得错误描述，不是错误代码  hujie
            onPayFail(response.getErr_code_des());
        } else {
            // 通信失败  hujie
            String return_msg="通讯错误，请检查网络";
            if(response!=null){
                return_msg = response.getReturn_msg();
            }
            onPayFail("支付失败,"+return_msg);
//            view.waitingQuary();
            tryWechatAliQuery();
        }
    }

    /**
     * 撤销(冲正)订单
     * @param reverseReason
     */
    private void doReverse(String reverseReason) {
        ReverseReqData request = new ReverseReqData("", cartManager.getSafeSaleNo());
        tencentReverseTask.setReqParam(request);
        this.reverseReason = reverseReason;
        tencentReverseTask.execute(reverseDisposable);
    }

    private void onWechatAliQueryReturn(ScanPayQueryResData response) {
        Log.e("qqq","微信查询接口回参----->"+new Gson().toJson(response));
//        if (response.isSuccess() && response.isTransactionSuccess()) {
        //这里去掉一个判断，其实下面已经有付款成功的判断 hujie
        if (response.isSuccess() ) {
            if (response.isPaid()) {
                currentSaleNoUsed();
                onPaySuccess(Money.createAsFen(response.getTotal_fee()), response.getOut_trade_no());
                queryCount.set(0);
            }else if (response.isWaitingCustomer()) {
                tryWechatAliQuery();
            } else {
                onPayFail("支付失败");
                queryCount.set(0);
            }
        } else {
            tryWechatAliQuery();
        }
    }

    private void wechatAliQuery() {
        ScanPayQueryReqData request = new ScanPayQueryReqData("", global.getShopNo()+cartManager.getSafeSaleNo());
        Log.e("qqq","微信查询接口入参----->"+new Gson().toJson(request));
//        tencentQueryTask.setReqParam(request);
//        tencentQueryTask.execute(queryDisposable);

        String requestxml=Util.objectToXml(request);
        OkHttpUtils
                .postString()
                .content(requestxml)
                .url(Configure.PAY_QUERY_API)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("qqq","微信查询接口回参----->"+e.getMessage());
//                        view.waitingQuary();
//                        onPayFail("微信查询超时，正在重试");
                        view.showMessage("查询超时，持续查询中");
                        tryWechatAliQuery();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ScanPayQueryResData objectFromXML =(ScanPayQueryResData) Util.getObjectFromXML(response, ScanPayQueryResData.class);
                        onWechatAliQueryReturn(objectFromXML);
                    }
                });
    }

    private void tryWechatAliQuery() {
        if (cancelQuery) {
            return;
        }
        if (queryCount.getAndIncrement() > QUERY_COUNT_MAX) {
            cancelQuery = true;
            // TODO: 暂时没有自动冲正
            onPayFail("支付失败");
            queryCount.set(0);
        } else {
            view.waitingCustomerPay();
            Observable.timer(5, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
//                            view.showMessage("第"+queryCount.get()+"次查询中");
                            wechatAliQuery();
                        }
                    });
        }
    }

    private void onPaySuccess(Money money, String relationNumber) {
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

    private void onPayFail(String reason) {
//        skipSaleNo();
        view.payFailed(reason);
    }

    // 微信/支付宝支付过后订单需要保存, 避免下次支付时失败
    private void currentSaleNoUsed() {
        settings.saveUsedSaleNo(cartManager.getSafeSaleNo());
    }

    private DefaultObserver payDisposable = new DefaultObserver<ScanPayResData>() {
        @Override
        public void onNext(ScanPayResData response) {
            onWechatAliPayReturn(response);
            dispose();
        }

        @Override
        public void onError(Throwable e) {
            onPayFail(e.getMessage());
        }
    };

    private DefaultObserver queryDisposable = new DefaultObserver<ScanPayQueryResData>() {
        @Override
        public void onNext(ScanPayQueryResData response) {
            onWechatAliQueryReturn(response);
        }

        @Override
        public void onError(Throwable e) {
            onPayFail(e.getMessage());
            queryCount.set(0);
        }
    };

    private DefaultObserver reverseDisposable = new DefaultObserver<ReverseResData>() {
        @Override
        public void onNext(ReverseResData response) {
            if (response.isSuccess()) {
                if (reverseReason != null) {
                    onPayFail("支付失败: " + reverseReason);
                } else {
                    onPayFail("支付失败, 已撤销订单. 如已扣款项, 将原路退回");
                }
            } else {
                // 撤销订单失败, 产生的单边账需要人工处理
                onPayFail("支付失败, 无法正常撤销订单, 请联系管理人员");
            }
            reverseReason = null;
        }

        @Override
        public void onError(Throwable e) {
            reverseReason = null;
            onPayFail(e.getMessage());
            queryCount.set(0);
        }
    };
}
