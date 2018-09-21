package com.tianhe.pay.ui.refund;

import com.tianhe.devices.Printer;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.payment.CouponProcess;
import com.tianhe.pay.data.crm.payment.PointProcess;
import com.tianhe.pay.data.crm.payment.ProcessPayment;
import com.tianhe.pay.data.crm.payment.ProcessPaymentResult;
import com.tianhe.pay.data.crm.payment.StoredValueCardPay;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoRequest;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class RefundOrderPresenter extends TianHePresenter<RefundOrderContract.View>
        implements RefundOrderContract.Presenter {

    Global global;
    RefundDataManager refundDataManager;
    UseCase submitOrderTask;
    UseCase querySaleNoTask;
    UseCase processPaymentTask; //处理卡系统支付
    Settings settings;
    PrintUseCase printTask;

    @Inject
    public RefundOrderPresenter(RefundDataManager refundDataManager,
                                @Named("refundOrder") UseCase submitOrderTask,
                                @Named("querySaleNo") UseCase querySaleNoTask,
                                @Named("processPayment") UseCase processPaymentTask,
                                PrintUseCase printTask,
                                Settings settings,
                                Global global) {
        this.refundDataManager = refundDataManager;
        this.global = global;
        this.settings = settings;
        this.submitOrderTask = submitOrderTask;
        this.querySaleNoTask = querySaleNoTask;
        this.processPaymentTask = processPaymentTask;
        this.printTask = printTask;
    }

    @Override
    public void prepareSaleNo() {
        if (canUseLocalSaleNo()) {
            setNewSaleNoByLocal();
            view.prepareSaleNoSuccess();
        } else {
            queryServerSaleNo();
        }
    }

    @Override
    public void refundOrder() {
        processPayment();
    }

    @Override
    public void printRefundOrder() {
        printTask.setReqParam(PrintUtils.billOrder(refundDataManager.forRefundOrder(), false, global.getSupplierName()));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.printRefundOrderFail(state.getMessage());
                } else {
                    view.printRefundOrderSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.printRefundOrderFail(e.getMessage());
            }
        });
    }

    private boolean canUseLocalSaleNo() {
        return settings.getTodayLastSaleNo() != null;
    }

    private void setNewSaleNoByServer(String serverMaxSaleNo) {
        long serverSn = Long.valueOf(serverMaxSaleNo.substring(serverMaxSaleNo.length() - 4));
        long localSn = Long.valueOf(settings.getLocalSerialNumber());
        long safeSn = Math.max(serverSn, localSn);
        String newSaleNo = serverMaxSaleNo.substring(0, serverMaxSaleNo.length() - 4) + String.format("%04d", safeSn);
        refundDataManager.setSafeSaleNo(newSaleNo);
    }

    private void setNewSaleNoByLocal() {
        String saleNoPrefix = global.getTerminalId() + Times.yyMMddHHmmss(new Date());
        String newSaleNo = saleNoPrefix + settings.getLocalSerialNumber();
        refundDataManager.setSafeSaleNo(newSaleNo);
    }

    private void queryServerSaleNo() {
        GetLastSaleNoRequest request = new GetLastSaleNoRequest();
        request.setTerminalId(global.getTerminalId());
        request.setDate(System.currentTimeMillis());
        querySaleNoTask.setReqParam(request);
        querySaleNoTask.execute(new DefaultObserver<String>() {
            @Override
            public void onNext(@NonNull String saleNo) {
                setNewSaleNoByServer(saleNo);
                view.prepareSaleNoSuccess();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.prepareSaleNoFail("获取退货单号失败");
            }
        });
    }

    private void realRefundOrder() {
        final Order order = refundDataManager.forRefundOrder();
        if (settings.isTraining()) {
            order.getOrderHeader().setIsPractice("Y");
        } else {
            order.getOrderHeader().setIsPractice("N");
        }
        submitOrderTask.setReqParam(order);
        submitOrderTask.execute(new DefaultObserver<SubmitOrderResult>() {
            @Override
            public void onNext(@NonNull SubmitOrderResult result) {
                refundDataManager.refundOrderSuccess(result);
                settings.saveUsedSaleNo(order.getOrderHeader().getSaleNo());
                view.refundOrderSuccess(result);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.refundOrderFail(e.getMessage());
            }
        });
    }

    private void processPayment() {
        List<CouponProcess> couponProcesses = refundDataManager.getCouponRefunds();
        List<StoredValueCardPay> cardPays = refundDataManager.getStoredValuePRefunds();
        BigDecimal pointTotal = refundDataManager.getRefundPointTotal();
        String vipNo = refundDataManager.getVipNo();
        boolean needProcessPoints = !Strings.isBlank(vipNo) && pointTotal.doubleValue() > 0;
        boolean needProcessPCrmPayment = (couponProcesses.size() > 0
                || cardPays.size() > 0
                || needProcessPoints);
        if (!needProcessPCrmPayment) {
            realRefundOrder();
            return;
        }
        ProcessPayment processPayment = new ProcessPayment(global.getShopNo(), settings.isTraining());
        processPayment.setSaleNo(refundDataManager.getSafeSaleNo());
        // 订单退款总额成负数
//        Money refundTotal = refundDataManager.forRefundOrder().getAdjustedTotal().negate();
        Money refundTotal = refundDataManager.getOriSaleAmountTotal().negate();

        processPayment.setAmount(refundTotal.toString());
        processPayment.setCoupons(couponProcesses);
        processPayment.setStoredValueCardPays(cardPays);
        if (needProcessPoints) {
            PointProcess pointProcess = new PointProcess();
            pointProcess.setSaleType();
            pointProcess.setAmount(refundTotal.toString());
            pointProcess.setCardNo(vipNo);
            // 设置积分为负数
            pointProcess.setPoint("-" + pointTotal.toString());
            pointProcess.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            processPayment.setPoint(pointProcess);
        }
        processPaymentTask.setReqParam(processPayment);
        processPaymentTask.execute(new DefaultObserver<ProcessPaymentResult>() {
            @Override
            public void onNext(@NonNull ProcessPaymentResult processPaymentResult) {
                // 返回的值没用,不需要处理
                realRefundOrder();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.refundOrderFail(e.getMessage());
            }
        });
    }

}
