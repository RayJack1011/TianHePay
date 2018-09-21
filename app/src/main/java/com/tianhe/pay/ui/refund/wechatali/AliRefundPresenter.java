package com.tianhe.pay.ui.refund.wechatali;

import android.util.Log;

import com.ali.demo.api.response.AlipayTradeRefundResponse;
import com.ali.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FRefundResult;
import com.google.gson.Gson;
import com.tianhe.devices.Printer;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class AliRefundPresenter extends TianHePresenter<WechatAliRefundContract.View>
        implements WechatAliRefundContract.Presenter {

    Global global;
    RefundDataManager refundDataManager;
    UseCase aliRefundTask;
    //    PaidInfo sourcePaid;
    String paymentId;
    PrintUseCase printTask;

    @Inject
    public AliRefundPresenter(RefundDataManager refundDataManager,
                              @Named("aliRefund") UseCase aliRefundTask,
                              PrintUseCase printTask,
                              Global global) {
        this.aliRefundTask = aliRefundTask;
        this.refundDataManager = refundDataManager;
        this.printTask = printTask;
        this.global = global;
    }

    @Override
    public void refund(String refundCode, String paymentId) {
        this.paymentId = paymentId;
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        if (signpost != PaymentSignpost.ALI) {
            view.refundFailed("不支持该支付方式退款!");
            return;
        }
        // 原单号与扫码/输入的单号不同
//        if (refundCode.equals(refundDataManager.getOriSaleNo())) {
//            view.refundFailed("不是相同交易单号, 无法退款!");
//            return;
//        }
        AlipayTradeRefundRequestBuilder request = createRefundRequest(refundCode);
        Log.e("qqq",new Gson().toJson(request)+":::::::::::::::;支付宝退款入参");
        aliRefundTask.setReqParam(request);
        aliRefundTask.execute(new DefaultObserver<AlipayF2FRefundResult>() {
            @Override
            public void onNext(@NonNull AlipayF2FRefundResult result) {
                Log.e("qqq",new Gson().toJson(result)+":::::::::::::::;支付宝退款回参（成功）");
                onRefundSuccess(result);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("qqq",new Gson().toJson(e.getMessage())+":::::::::::::::;支付宝退款回参（失败）");
                onRefundFail(e.getMessage());
            }
        });
    }

    /**
     * @param refundCode 扫码到的退款二维码, 原生支付宝接口是原商户单号
     * @return
     */
    private AlipayTradeRefundRequestBuilder createRefundRequest(String refundCode) {
//        int moneys = new BigDecimal(CommomData.money).multiply(new BigDecimal(100)).intValue();
        AlipayTradeRefundRequestBuilder refundRequest = new AlipayTradeRefundRequestBuilder()
                // 原退款单号
                .setOutTradeNo(refundCode)
                .setRefundAmount(CommomData.money)
                .setRefundReason("正常退款")
                // 退款单号
                .setOutRequestNo(refundDataManager.getSafeSaleNo())
                .setStoreId(global.getShopNo());
        return refundRequest;
    }

    @Override
    public void printRefund(final PaidInfo refund) {
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
                    view.printRefundFail(state.getMessage(),refund);
//                    view.printRefundFail(state.getMessage());
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

    private void onRefundSuccess(AlipayF2FRefundResult result) {
        AlipayTradeRefundResponse response = result.getResponse();
        if (!result.isTradeSuccess()) {
            if (response != null) {
                view.refundFailed(response.getSubMsg());
            } else {
                view.refundFailed("退款失败!");
            }
            return;
        }
        PaidInfo refundPaid = new PaidInfo();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        PaidInfo source = refundDataManager.findRefundableOriByPaymentId(paymentId);

        refundPaid.setPaymentId(signpost.getPaymentId());
        long moneys = Long.valueOf(new BigDecimal(CommomData.money).multiply(new BigDecimal(100)).intValue()+"");
//        long moneys = Long.valueOf(response.getSendBackFee());
        refundPaid.setSaleAmount(new Money(moneys));
        // 支付宝退款接口没有产生支付宝的退款单号返回, 使用原支付宝单号
        refundPaid.setBillNo(result.getResponse().getOutTradeNo());
        refundPaid.setPaymentName("支付宝");
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        view.refundSuccess(refundPaid);
    }

    private void onRefundFail(String reason) {
        view.refundFailed(reason);
    }
}
