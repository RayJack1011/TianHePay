package com.tianhe.pay.ui.refund.wechatali;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.devices.Printer;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.data.wechatali.query.TongguanQueryRequest;
import com.tianhe.pay.data.wechatali.refund.TongguanRefundRequest;
import com.tianhe.pay.data.wechatali.refund.TongguanRefundResponse;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.utils.Times;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class WechatAliRefundPresenter extends TianHePresenter<WechatAliRefundContract.View>
        implements WechatAliRefundContract.Presenter {

    Global global;
    RefundDataManager refundDataManager;
    UseCase refundTask;
//    PaidInfo sourcePaid;
    String paymentId;
    PrintUseCase printTask;

    @Inject
    public WechatAliRefundPresenter(RefundDataManager refundDataManager,
            @Named("wechatAliRefund") UseCase refundTask,
                                    PrintUseCase printTask,
                                    Global global) {
        this.refundTask = refundTask;
        this.refundDataManager = refundDataManager;
        this.printTask = printTask;
        this.global = global;
    }

    @Override
    public void refund(String refundCode, String paymentId) {
        this.paymentId = paymentId;
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        if (signpost != PaymentSignpost.ALI && signpost != PaymentSignpost.WECHAT) {
            view.refundFailed("不支持该支付方式退款!");
            return;
        }
        TongguanRefundRequest request = new TongguanRefundRequest();
        request.setAccount(global.getWechatAliAccount());
        request.setKey(global.getWechatAliKey());
        request.setUpOrderId(refundCode);
        request.setLowOrderId(refundDataManager.getOriSaleNo());
        Log.e("qqq",new Gson().toJson(request)+":::::::;退款入参");
        refundTask.setReqParam(request);
        refundTask.execute(new DefaultObserver<TongguanRefundResponse>() {
            @Override
            public void onNext(@NonNull TongguanRefundResponse tongguanRefundResponse) {
                Log.e("qqq",new Gson().toJson(tongguanRefundResponse)+":::::::::::::::::退款回参");
                onRefundSuccess(tongguanRefundResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onRefundFail(e.getMessage());
            }
        });
    }

    @Override
    public void printRefund(PaidInfo refund) {
        WechatAliOrder wechatAliOrder = new WechatAliOrder();
        wechatAliOrder.setDatetime(refund.getTime());
        wechatAliOrder.setPayName(refund.getPaymentName());
        wechatAliOrder.setLowOrderId(refund.getBillNo());
        wechatAliOrder.setUpOrderId(refund.getBillNo());
        wechatAliOrder.setOrderState("退款");
        wechatAliOrder.setShopNo(global.getShopNo());
        wechatAliOrder.setUserNo(global.getUserNo());
        wechatAliOrder.setTerminalNo(global.getTerminalId());
        wechatAliOrder.setPayMoney(refund.getSaleAmount());

        printTask.setReqParam(PrintUtils.wechatAliPrint(wechatAliOrder));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.printRefundFail(state.getMessage());
                } else {
                    view.printRefundSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.printRefundFail(e.getMessage());
            }
        });
    }

    private void onRefundSuccess(TongguanRefundResponse response) {
        if (!response.isSuccess()) {
            view.refundFailed(response.getMessage());
            return;
        }
        PaidInfo refundPaid = new PaidInfo();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        PaidInfo source = refundDataManager.findRefundableOriByPaymentId(paymentId);

        refundPaid.setPaymentId(signpost.getPaymentId());
        refundPaid.setSaleAmount(source.getSaleAmount());
        refundPaid.setBillNo(response.getUpOrderId());
        refundPaid.setPaymentName(source.getPaymentName());
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        view.refundSuccess(refundPaid);
    }

    private void onRefundFail(String reason) {
        view.refundFailed(reason);
    }
}
