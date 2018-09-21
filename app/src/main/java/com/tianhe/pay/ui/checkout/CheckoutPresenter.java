package com.tianhe.pay.ui.checkout;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.devices.Printer;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.gift.GiftCoupon;
import com.tianhe.pay.data.crm.gift.ProcessGift;
import com.tianhe.pay.data.crm.gift.ProcessGiftResult;
import com.tianhe.pay.data.crm.gift.ProcessGiftUseCase;
import com.tianhe.pay.data.crm.payment.PointProcess;
import com.tianhe.pay.data.order.calculate.Coupon;
import com.tianhe.pay.data.order.calculate.Gift;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoRequest;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.data.crm.payment.CouponProcess;
import com.tianhe.pay.data.crm.payment.ProcessPayment;
import com.tianhe.pay.data.crm.payment.ProcessPaymentResult;
import com.tianhe.pay.data.crm.payment.StoredValueCardPay;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Times;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class CheckoutPresenter extends TianHePresenter<CheckoutContract.View> implements CheckoutContract.Presenter {

    private UseCase calculateTask;
    private UseCase getMaxSaleNoTask;
    private UseCase submitBillTask;
    private UseCase processPaymentTask; //处理卡系统支付
    private UseCase processGiftTask;    //处理赠送卡/券
    private CartManager cartManager;
    private Global global;
    private Settings settings;
    private PrintUseCase printTask;

    @Inject
    public CheckoutPresenter(@Named("calculate") UseCase calculateTask,
                             @Named("maxSaleNo") UseCase getMaxSaleNoTask,
                             CartManager cartManager, Global global, Settings settings,
                             @Named("submitOrder") UseCase submitBillTask,
                             @Named("processPayment") UseCase processPaymentTask,
                             PrintUseCase printTask, ProcessGiftUseCase processGiftUseCase) {
        this.calculateTask = calculateTask;
        this.getMaxSaleNoTask = getMaxSaleNoTask;
        this.cartManager = cartManager;
        this.global = global;
        this.settings = settings;
        this.submitBillTask = submitBillTask;
        this.processPaymentTask = processPaymentTask;
        this.printTask = printTask;
        this.processGiftTask = processGiftUseCase;
    }

    @Override
    public void calculateOrder() {
        // before calculate, get sale number
        if (canUseLocalSaleNo()) {
            setNewSaleNoByLocal();
            realCalculate();
        } else {
            getMaxSaleNo();
        }
    }

    @Override
    public void loadUsablePayment() {
        List<Payment> usablePayments = global.getUsablePayments();
        view.renderUsablePayments(usablePayments);
    }

    @Override
    public void submitOrder() {
        // 先处理卡系统的支付信息，成功后才提交账单信息
        // 纸质卷支付, 需要上传到crm
        List<CouponProcess> couponProcesses = cartManager.getCouponPays();
        // 储值卡支付, 需要上传到crm
        List<StoredValueCardPay> cardPays = cartManager.getStoredValuePays();

        boolean hasVip = cartManager.hasVip();
        BigDecimal pointTotal = cartManager.getPointTotal();
        boolean needProcessPoints = hasVip && pointTotal.doubleValue() > 0;

        boolean needProcessPCrmPayment = (couponProcesses.size() > 0
                || cardPays.size() > 0
                || needProcessPoints);
        if (!needProcessPCrmPayment) {
            realSubmitOrder();
            return;
        }
        ProcessPayment processPayment = new ProcessPayment(global.getShopNo(), settings.isTraining());
        processPayment.setSaleNo(cartManager.getSafeSaleNo());
        processPayment.setAmount(cartManager.getAdjustedTotal().toString());
        processPayment.setCoupons(couponProcesses);
        processPayment.setStoredValueCardPays(cardPays);
        if (needProcessPoints) {
            PointProcess pointProcess = new PointProcess();
            pointProcess.setSaleType();
            pointProcess.setAmount(cartManager.getAdjustedTotal().toString());
            pointProcess.setCardNo(cartManager.currentVip().getCard().cardNo);
            pointProcess.setPoint(pointTotal.toString());
            pointProcess.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            processPayment.setPoint(pointProcess);
        }
        processPaymentTask.setReqParam(processPayment);
        processPaymentTask.execute(new DefaultObserver<ProcessPaymentResult>() {
            @Override
            public void onNext(@NonNull ProcessPaymentResult processPaymentResult) {
                // 返回的值没用,不需要处理
                realSubmitOrder();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.submitOrderFail(e.getMessage());
            }
        });
    }

    @Override
    public void printOrder() {
        printTask.setReqParam(PrintUtils.billOrder(cartManager.forSubmitOrder(), false, global.getSupplierName()));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.printOrderFail(state.getMessage());
                } else {
                    view.printOrderSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.printOrderFail(e.getMessage());
            }
        });
    }

    private void realCalculate() {
        calculateTask.setReqParam(cartManager.forCalculate());
        calculateTask.execute(new DefaultObserver<CalculationResult>() {
            @Override
            public void onNext(@NonNull CalculationResult result) {
                Log.e("qqq", new Gson().toJson(result.getCouponList()) + "::::::couponlist");
                cartManager.onCalculated(result);
                view.calculateSuccess();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.calculateFail(e.getMessage());
            }
        });
    }

    private void getMaxSaleNo() {
        GetLastSaleNoRequest request = new GetLastSaleNoRequest();
        request.setTerminalId(global.getTerminalId());
        request.setDate(System.currentTimeMillis());
        getMaxSaleNoTask.setReqParam(request);
        getMaxSaleNoTask.execute(new DefaultObserver<String>() {
            @Override
            public void onNext(@NonNull String saleNo) {
                setNewSaleNoByServer(saleNo);
                realCalculate();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.calculateFail(e.getMessage());
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
        cartManager.setSafeSaleNo(newSaleNo);
    }

    private void setNewSaleNoByLocal() {
        String saleNoPrefix = global.getTerminalId() + Times.yyMMddHHmmss(new Date());
        String newSaleNo = saleNoPrefix + settings.getLocalSerialNumber();
        cartManager.setSafeSaleNo(newSaleNo);
    }
    private void realSubmitOrder() {
        Order order = cartManager.forSubmitOrder();
        if (settings.isTraining()) {
            order.getOrderHeader().setIsPractice("Y");
        } else {
            order.getOrderHeader().setIsPractice("N");
        }
        submitBillTask.setReqParam(order);
        submitBillTask.execute(new DefaultObserver<SubmitOrderResult>() {
            @Override
            public void onNext(@NonNull SubmitOrderResult submitOrderResult) {
                cartManager.submitOrderSuccess(submitOrderResult);
                settings.saveUsedSaleNo(cartManager.getSafeSaleNo());
                view.submitOrderSuccess(submitOrderResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.submitOrderFail(e.getMessage());
            }
        });
    }

    public void giveCoupon() {
        List<Gift> coupon = cartManager.getCouponList();
    }

    // 只处理赠送纸质券, 没有处理赠送卡
    public void processGift() {
        List<Gift> gifts = cartManager.getCouponGift();
        ProcessGift processRequest = new ProcessGift(global.getShopNo(), settings.isTraining());
        processRequest.setSaleNo(cartManager.getSafeSaleNo());
        processRequest.setShopNo(global.getShopNo());
        // 单据类型为销售单
        processRequest.setSaleType();
        List<GiftCoupon> giftCoupons = new ArrayList<>();
        for (Gift gift : gifts) {
            GiftCoupon coupon = new GiftCoupon();
            // 设置状态为发售
            coupon.setPublishStatus();
            coupon.setType(gift.getType());
            coupon.setQuantity(String.valueOf(gift.getQuantity()));
            coupon.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            giftCoupons.add(coupon);
        }
        processRequest.setGiftCoupons(giftCoupons);

//        view.processGiftSuccess(processRequest);
        processGiftTask.setReqParam(processRequest);
        processGiftTask.execute(new DefaultObserver<ProcessGiftResult>() {
            @Override
            public void onNext(ProcessGiftResult gift) {
                if(gift != null&&gift.getGiftCoupons().size() > 0){
                    view.processGiftSuccess(gift);
                }else{
                    view.processGiftFail("券已经赠送完");
                }
            }

            @Override
            public void onError(Throwable e) {
                view.processGiftFail(e.getMessage());
            }
        });
    }

    // 打印纸质券
    @Override
    public void printGiftCoupon(List<GiftCoupon> giftCoupons) {
        printTask.setReqParam(PrintUtils.paperCoupon(global, giftCoupons));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.printGiftCouponFail(state.getMessage());
                } else {
                    view.printGiftCouponSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.printGiftCouponFail(e.getMessage());
            }
        });
    }
}
