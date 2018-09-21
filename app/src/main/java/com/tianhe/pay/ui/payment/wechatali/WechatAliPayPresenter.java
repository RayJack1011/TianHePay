package com.tianhe.pay.ui.payment.wechatali;

import com.tianhe.devices.Printer;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.data.wechatali.pay.TongguanPayRequest;
import com.tianhe.pay.data.wechatali.pay.TongguanPayResponse;
import com.tianhe.pay.data.wechatali.query.TongguanQueryRequest;
import com.tianhe.pay.data.wechatali.query.TongguanQueryResponse;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class WechatAliPayPresenter extends TianHePresenter<WechatAliPayContract.View> implements WechatAliPayContract.Presenter {
    private static final int QUERY_COUNT_MAX = 20;

    protected Global global;
    protected CartManager cartManager;

    Settings settings;
    UseCase wechatAliPayTask;
    UseCase wechatAliQueryTask;
    UseCase wechatAliReverseTask;
    PrintUseCase printTask;
    AtomicInteger queryCount = new AtomicInteger(0);
    transient boolean cancelQuery;

    Payment inFlightPayment;

    @Inject
    public WechatAliPayPresenter(Global global, CartManager cartManager, Settings settings,
                                 @Named("wechatAliPay") UseCase wechatAliPayTask,
                                 @Named("wechatAliQuery") UseCase wechatAliQueryTask,
                                 @Named("wechatAliReverse") UseCase wechatAliReverseTask,
                                 PrintUseCase printTask) {
        this.global = global;
        this.cartManager = cartManager;
        this.settings = settings;
        this.wechatAliPayTask = wechatAliPayTask;
        this.wechatAliQueryTask = wechatAliQueryTask;
        this.wechatAliReverseTask = wechatAliReverseTask;
        this.printTask = printTask;
    }

    @Override
    public void pay(Payment payment, Money payAmount, String code) {
        inFlightPayment = payment;
        currentSaleNoUsed();
        cancelQuery = false;
        TongguanPayRequest request = createPayRequest(payAmount, code);
        wechatAliPayTask.setReqParam(request);
        wechatAliPayTask.execute(payDisposable);
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
        wechatAliOrder.setLowOrderId(paidInfo.getBillNo());
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

    private TongguanPayRequest createPayRequest(Money payAmount, String code) {

        TongguanPayRequest request = new TongguanPayRequest();
        request.setAccount(global.getWechatAliAccount());
        request.setKey(global.getWechatAliKey());
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(inFlightPayment.getPaymentId());
        if (signpost == PaymentSignpost.ALI) {
            request.setAliType();
        } else if (signpost == PaymentSignpost.WECHAT) {
            request.setWechatPayType();
        }
        request.setBarcode(code);
        request.setBody(cartManager.getSafeSaleNo());
        request.setPayMoney(payAmount);
        request.setLowCashier(global.getUserNo());
        request.setLowOrderId(cartManager.getSafeSaleNo());
        return request;
    }

    private void onWechatAliPayReturn(TongguanPayResponse response) {
        if (response.isSuccess()) {
            if (response.isTransactionSuccess()) {
                currentSaleNoUsed();
                onPaySuccess(response.getPayMoney(), response.getUpOrderId());
            }
        } else if (response.isWaitingCustomer()) {
            // "等待用户付款"
            tryWechatAliQuery();
        } else {
            String errorMsg = response.getMessage();
            if (Strings.isBlank(errorMsg)) {
                onPayFail("支付失败");
            } else {
                onPayFail(response.getMessage());
            }
        }
    }

    private void wechatAliQuery() {
        TongguanQueryRequest request = createQueryRequest();
        wechatAliQueryTask.setReqParam(request);
        wechatAliQueryTask.execute(queryDisposable);
    }

    private TongguanQueryRequest createQueryRequest() {
        TongguanQueryRequest request = new TongguanQueryRequest();
        request.setAccount(global.getWechatAliAccount());
        request.setKey(global.getWechatAliKey());
        request.setLowOrderId(cartManager.getSafeSaleNo());
        return request;
    }

    private void onWechatAliQueryReturn(TongguanQueryResponse response) {
        if (response.isSuccess()) {
            if (response.isTransactionSuccess()) {
                currentSaleNoUsed();
                onPaySuccess(response.getPayMoney(), response.getUpOrderId());
                queryCount.set(0);
            }else if (response.isWaitingCustomer()) {
                tryWechatAliQuery();
            } else if (response.isCancel()) {
                onPayFail("客户取消支付!");
                queryCount.set(0);
            } else {
                onPayFail("支付失败");
                queryCount.set(0);
            }
        } else {
            tryWechatAliQuery();
        }
    }

    private void tryWechatAliQuery() {
        if (cancelQuery) {
            return;
        }
        if (queryCount.getAndIncrement() > QUERY_COUNT_MAX) {
            // TODO: 暂时没有自动冲正
            onPayFail("支付失败");
            queryCount.set(0);
        } else {
            view.waitingCustomerPay();
            Observable.timer(5, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
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
//        settings.saveWechatAli(relationNumber);
        settings.saveWechatAli(cartManager.getSafeSaleNo());
        view.paySuccess(paidInfo);
    }

    private void onPayFail(String reason) {
        skipSaleNo();
        view.payFailed(reason);
    }

    // 微信/支付宝支付过后订单需要保存, 避免下次支付时失败
    private void currentSaleNoUsed() {
        settings.saveUsedSaleNo(cartManager.getSafeSaleNo());
    }

    private DefaultObserver payDisposable = new DefaultObserver<TongguanPayResponse>() {
        @Override
        public void onNext(TongguanPayResponse response) {
            onWechatAliPayReturn(response);
        }

        @Override
        public void onError(Throwable e) {
            onPayFail(e.getMessage());
        }
    };

    private DefaultObserver queryDisposable = new DefaultObserver<TongguanQueryResponse>() {
        @Override
        public void onNext(TongguanQueryResponse response) {
            onWechatAliQueryReturn(response);
        }

        @Override
        public void onError(Throwable e) {
            onPayFail(e.getMessage());
            queryCount.set(0);
        }
    };
}
