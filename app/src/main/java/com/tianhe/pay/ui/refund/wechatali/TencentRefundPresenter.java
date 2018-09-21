package com.tianhe.pay.ui.refund.wechatali;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_protocol.RefundResData;
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
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class TencentRefundPresenter extends TianHePresenter<WechatAliRefundContract.View>
        implements WechatAliRefundContract.Presenter {

    Global global;
    RefundDataManager refundDataManager;
    UseCase tencentRefundTask;
//    PaidInfo sourcePaid;
    String paymentId;
    PrintUseCase printTask;

    @Inject
    public TencentRefundPresenter(RefundDataManager refundDataManager,
                                  @Named("tencentRefund") UseCase tencentRefundTask,
                                  PrintUseCase printTask,
                                  Global global) {
        this.tencentRefundTask = tencentRefundTask;
        this.refundDataManager = refundDataManager;
        this.printTask = printTask;
        this.global = global;
    }

    @Override
    public void refund(final String refundCode, String paymentId) {
        this.paymentId = paymentId;
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        if (signpost != PaymentSignpost.WECHAT) {
            view.refundFailed("不支持该支付方式退款!");
            return;
        }
        // 原单号与扫码/输入的单号不同
//        if (refundCode.equals(refundDataManager.getOriSaleNo())) {
//            view.refundFailed("不是相同交易单号, 无法退款!");
//            return;
//        }
        RefundReqData request = createRefundRequest(refundCode);
        Log.e("qqq","微信退款入参----->"+new Gson().toJson(request)+":::JJJJJJJJJJJJJJ:::"+CommomData.money);
        tencentRefundTask.setReqParam(request);
        tencentRefundTask.execute(new DefaultObserver<RefundResData>() {
            @Override
            public void onNext(@NonNull RefundResData refundResponse) {
                Log.e("qqq","微信退款回参（成功）----->"+new Gson().toJson(refundResponse));
//                refundResponse.gets
                onRefundSuccess(refundResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("qqq","微信退款回参(失败)----->"+new Gson().toJson(e.getMessage()));
                onRefundFail(e.getMessage());
            }

        });
    }

    private RefundReqData createRefundRequest(String refundCode) {
        Log.e("qqq",CommomData.money+":::::::::::::;;hehhe::::::"+refundDataManager.getOriSaleNo());
        int payTypeMoney = 0;
        for(PaidInfo paidInfo:refundDataManager.getOriPaidInfos()){
            if(paidInfo.getPaymentId().equals("03")){
                Log.e("qqq","支付方式金额----->"+paidInfo.getSaleAmount());
                payTypeMoney = new BigDecimal(paidInfo.getSaleAmount().toString()).multiply(new BigDecimal(100)).intValue();
            }
        }
        Log.e("qqq","支付方式付款----->"+refundDataManager.getRefundedTotal().getAmount());
        int refundTotal = (int) refundDataManager.getOriSaleAmountTotal().getAmount();
        int moneys = new BigDecimal(CommomData.money).multiply(new BigDecimal(100)).intValue();
        RefundReqData.Builder builder = RefundReqData.newBuilder();
        builder.setBizParams("", CommomData.tracHost,
                refundDataManager.getSafeSaleNo(), payTypeMoney, moneys);
        return builder.build();
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

    private void onRefundSuccess(RefundResData response) {
        if (!response.isSuccess()) {
            view.refundFailed("退款失败:" + response.getReturn_msg());
            return;
        }
        PaidInfo refundPaid = new PaidInfo();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        PaidInfo source = refundDataManager.findRefundableOriByPaymentId(paymentId);

        refundPaid.setPaymentId(signpost.getPaymentId());
//        refundPaid.setSaleAmount(source.getSaleAmount());
        long moneys = Long.valueOf(response.getRefund_fee());
        refundPaid.setSaleAmount(new Money(moneys));
        refundPaid.setBillNo(response.getOut_trade_no());
        refundPaid.setPaymentName(source.getPaymentName());
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        view.refundSuccess(refundPaid);
    }

    private void onRefundFail(String reason) {
        view.refundFailed(reason);
    }
}
