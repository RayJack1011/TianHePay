package com.tianhe.pay.model;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.crm.payment.CouponProcess;
import com.tianhe.pay.data.crm.payment.StoredValueCardPay;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderHeader;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;

public class RefundDataManager {
    private Global global;
    private String version;
    private Order orderSource;
    private Order refundOrder;
    private SensitiveValues sensitiveValues;

    private Map<PaymentSignpost, RefundablePay> refundablePays = new HashMap<>();
    private static final String REFUNDED_ITEM = "refundChanged";
    private transient BehaviorRelay<String> refundedTotalChanged = BehaviorRelay.create();

    public RefundDataManager(Global global, String version) {
        this.global = global;
        this.version = version;
        sensitiveValues = new SensitiveValues();
    }

    public void prepareForRefund(Order orderSource) {
        this.clear();
        if (orderSource == null) {
            return;
        }
        this.orderSource = orderSource;
        refundOrder = new Order();
        OrderHeader header = new OrderHeader();
        header.setShopNo(global.getShopNo());
        header.setTerminalId(global.getTerminalId());
        header.setUserNo(global.getUserNo());
        header.setVipNo(orderSource.getOrderHeader().getVipNo());
        header.setSystemVersion(version);
        header.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        header.setSaleNoSource(orderSource.getOrderHeader().getSaleNo());
        refundOrder.setOrderHeader(header);

        List<OrderItem> refundItems = new ArrayList<>();
        for (OrderItem item : orderSource.getOrderItems()) {
            OrderItem refundItem = new OrderItem();
            refundItem.setBarcode(item.getBarcode());
            refundItem.setName(item.getName());
            refundItem.setQuantity(item.getQuantity());
            refundItem.setOldPrice(item.getOldPrice());
            refundItem.setPrice(item.getPrice());
            refundItem.setSaleAmount(item.getSaleAmount());
            refundItem.setOldOrdinal(item.getOrdinal());
            refundItem.setOrdinal(item.getOrdinal());
            refundItem.setIntegral(item.getIntegral());
            refundItems.add(refundItem);
        }
        refundOrder.setOrderItems(refundItems);
        boolean isWx = false;
        boolean isAli = false;

        // 原单退款方式
        for (PaidInfo paidInfo : orderSource.getPaidInfos()) {
            PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paidInfo.getPaymentId());
//            if (signpost == PaymentSignpost.BANKCARD_POS
//                    || signpost == PaymentSignpost.WECHAT_POS
//                    || signpost == PaymentSignpost.ALI_POS) {
//                continue;
//            }
            if (signpost == PaymentSignpost.ALI) {
                isAli = true;
            }
            if (signpost == PaymentSignpost.WECHAT) {
                isWx = true;
            }
            RefundablePay refundable = refundablePays.get(signpost);
            if (refundable == null) {
                refundable = new RefundablePay();
                refundable.setSignpost(signpost);
                refundable.setPaymentName(paidInfo.getPaymentName());
            }
            refundable.addRefundableTotal(paidInfo.getSaleAmount().getAmount());
            // 微信支付宝、券,退款必须原单退
            if (signpost == PaymentSignpost.ALI || signpost == PaymentSignpost.WECHAT) {
                refundable.addLimitRelNo(paidInfo.getBillNo());
                refundable.setLimitRefundMax(paidInfo.getSaleAmount().getAmount());
            } else if (signpost == PaymentSignpost.COUPON) {
                refundable.addLimitRelNo(paidInfo.getBillNo());
            }
            refundablePays.put(signpost, refundable);
        }
        //TODO         可变更的退款方式(暂时不能使用)
        if (isAli&&!isWx) {
            RefundablePay extraRefundable4 = refundablePays.get(PaymentSignpost.WECHAT);
            if (extraRefundable4 == null) {
                extraRefundable4 = new RefundablePay();
                extraRefundable4.setSignpost(PaymentSignpost.WECHAT);
                extraRefundable4.setPaymentName("微信");
            }
            refundablePays.put(PaymentSignpost.WECHAT, extraRefundable4);
        }
        if (isWx&&!isAli) {
            //TODO         可变更的退款方式(暂时不能使用)
            RefundablePay extraRefundables3 = refundablePays.get(PaymentSignpost.ALI);
            if (extraRefundables3 == null) {
                extraRefundables3 = new RefundablePay();
                extraRefundables3.setSignpost(PaymentSignpost.ALI);
                extraRefundables3.setPaymentName("支付宝");
            }
            refundablePays.put(PaymentSignpost.ALI, extraRefundables3);
        }
        if(!isAli&&!isWx){
            //TODO         可变更的退款方式(暂时不能使用)
            RefundablePay extraRefundables3 = refundablePays.get(PaymentSignpost.ALI);
            if (extraRefundables3 == null) {
                extraRefundables3 = new RefundablePay();
                extraRefundables3.setSignpost(PaymentSignpost.ALI);
                extraRefundables3.setPaymentName("支付宝");
            }
            refundablePays.put(PaymentSignpost.ALI, extraRefundables3);

            RefundablePay extraRefundable4 = refundablePays.get(PaymentSignpost.WECHAT);
            if (extraRefundable4 == null) {
                extraRefundable4 = new RefundablePay();
                extraRefundable4.setSignpost(PaymentSignpost.WECHAT);
                extraRefundable4.setPaymentName("微信");
            }
            refundablePays.put(PaymentSignpost.WECHAT, extraRefundable4);
        }

        //TODO         可变更的退款方式(暂时不能使用)
        RefundablePay extraRefundables = refundablePays.get(PaymentSignpost.WECHAT_POS);
        if (extraRefundables == null) {
            extraRefundables = new RefundablePay();
            extraRefundables.setSignpost(PaymentSignpost.WECHAT_POS);
            extraRefundables.setPaymentName("微信离线");
        }
        refundablePays.put(PaymentSignpost.WECHAT_POS, extraRefundables);

        //TODO         可变更的退款方式(暂时不能使用)
        RefundablePay extraRefundables1 = refundablePays.get(PaymentSignpost.ALI_POS);
        if (extraRefundables1 == null) {
            extraRefundables1 = new RefundablePay();
            extraRefundables1.setSignpost(PaymentSignpost.ALI_POS);
            extraRefundables1.setPaymentName("支付宝离线");
        }
        refundablePays.put(PaymentSignpost.ALI_POS, extraRefundables1);

        //TODO         可变更的退款方式(暂时不能使用)
        RefundablePay extraRefundables2 = refundablePays.get(PaymentSignpost.BANKCARD_POS);
        if (extraRefundables2 == null) {
            extraRefundables2 = new RefundablePay();
            extraRefundables2.setSignpost(PaymentSignpost.BANKCARD_POS);
            extraRefundables2.setPaymentName("银行卡离线");
        }
        refundablePays.put(PaymentSignpost.BANKCARD_POS, extraRefundables2);

        // 可变更的退款方式(暂时不能使用)
        RefundablePay extraRefundable3 = refundablePays.get(PaymentSignpost.STOREDVALUE_CARD);
        if (extraRefundable3 == null) {
            extraRefundable3 = new RefundablePay();
            extraRefundable3.setSignpost(PaymentSignpost.STOREDVALUE_CARD);
            extraRefundable3.setPaymentName("储值卡");
        }
        refundablePays.put(PaymentSignpost.STOREDVALUE_CARD, extraRefundable3);
    }

    public List<RefundablePay> filterRefundable() {
        return new ArrayList<>(refundablePays.values());
    }

    public RefundablePay findRefundable(PaymentSignpost signpost) {
        return refundablePays.get(signpost);
    }

    public Observable<String> refundedChanged() {
        return refundedTotalChanged;
    }

    public Money getRefundedTotal() {
        return sensitiveValues.refundedTotal();
    }

    public BigDecimal getRefundPointTotal() {
        return refundOrder.getPointTotal();
    }

    public Money getRemainRefund() {
        return orderSource.getAdjustedTotal().subtract(getRefundedTotal());
    }

    public Money getOriSaleAmountTotal() {
        return orderSource.getAdjustedTotal();
    }

    public String getOriSaleNo() {
        return orderSource.getOrderHeader().getSaleNo();
    }

    public List<PaidInfo> getOriPaidInfos() {
        return orderSource.getPaidInfos();
    }

    public String getVipNo() {
        return refundOrder.getOrderHeader().getVipNo();
    }

    public boolean hasRefundPaidComplete() {
        return sensitiveValues.refundedTotal().getAmount()
                == orderSource.getAdjustedTotal().getAmount();
    }

    public boolean isRefunding() {
        if (orderSource == null) {
            return false;
        }
        return sensitiveValues.refundPaidSize() > 0;
    }

    public void addRefundPaid(PaidInfo paidInfo) {
        sensitiveValues.addRefundPaid(paidInfo);
        refundedTotalChanged.accept(REFUNDED_ITEM);

        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paidInfo.getPaymentId());
        RefundablePay refundablePay = findRefundable(signpost);
        refundablePay.changeRefunded(paidInfo.getSaleAmount().getAmount());
    }

    /**
     * 根据关联号查询原支付信息
     */
    public PaidInfo findRefundableOriByRelNo(String relNo) {
        for (PaidInfo paidInfo : orderSource.getPaidInfos()) {
            if (paidInfo.getPaymentId().equals(PaymentSignpost.BANKCARD.getPaymentId())) {//银行卡
//                if (relNo.equals(paidInfo.getCardNo())) {
                return paidInfo;
//                }
            } else {
                if (relNo.equals(paidInfo.getBillNo())) {
                    return paidInfo;
                }
            }

        }
        return null;
    }

    /**
     * 根据支付方式编号查询原支付信息
     */
    public PaidInfo findRefundableOriByPaymentId(String paymentId) {
        for (PaidInfo paidInfo : orderSource.getPaidInfos()) {
            if (paymentId.equals(paidInfo.getPaymentId())) {
                return paidInfo;
            }
        }
        return null;
    }

    public List<CouponProcess> getCouponRefunds() {
        List<CouponProcess> couponProcesses = new ArrayList<>();
        for (PaidInfo paidInfo : sensitiveValues.refundedPaid) {
            if (paidInfo.getPaymentId().equals(PaymentSignpost.COUPON.getPaymentId())) {
                CouponProcess couponProcess = new CouponProcess();
                couponProcess.setStartCouponNo(paidInfo.getBillNo());
                couponProcess.setEndCouponNo(paidInfo.getBillNo());
                couponProcess.setRefundState();
                couponProcess.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                couponProcesses.add(couponProcess);
            }
        }
        return couponProcesses;
    }

    public List<StoredValueCardPay> getStoredValuePRefunds() {
        List<StoredValueCardPay> storedValueCardPays = new ArrayList<>();
        for (PaidInfo paidInfo : sensitiveValues.refundedPaid) {
            if (paidInfo.getPaymentId().equals(PaymentSignpost.STOREDVALUE_CARD.getPaymentId())) {
                StoredValueCardPay pay = new StoredValueCardPay();
                pay.setCardNo(paidInfo.getBillNo());
                // 退款设置成负数
                pay.setAmount(paidInfo.getSaleAmount().negate().toString());
                pay.setPassword(paidInfo.getPwd());
                pay.setVerifyCode(paidInfo.getMarkCode());
                pay.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                storedValueCardPays.add(pay);
            }
        }
        return storedValueCardPays;
    }

    public Order forRefundOrder() {
        if (refundOrder.getPaidInfos() == null) {
            refundOrder.getOrderHeader().setSaleNo(sensitiveValues.saleNo);
            refundOrder.setPaidInfos(sensitiveValues.refundedPaid);
        }
        return refundOrder;
    }

    public void refundOrderSuccess(SubmitOrderResult result) {
        if (refundOrder != null) {
            refundOrder.getOrderHeader().setTime(result.getOpTime());
        }
    }

    public String getSafeSaleNo() {
        return sensitiveValues.saleNo;
    }

    public void setSafeSaleNo(String saleNo) {
        sensitiveValues.saleNo = saleNo;
    }

    public void clear() {
        orderSource = null;
        refundOrder = null;
        sensitiveValues.reset();
        refundablePays.clear();
//        refundedTotal.hasValue();
    }

    private static class SensitiveValues {
        String saleNo;
        String userNo;
        List<PaidInfo> refundedPaid;
        Money refundedTotal;

        void reset() {
            saleNo = null;
            userNo = null;
            refundedPaid = null;
            refundedTotal = Money.zeroMoney();
        }

        int refundPaidSize() {
            if (refundedPaid == null) {
                return 0;
            }
            return refundedPaid.size();
        }

        void addRefundPaid(PaidInfo paidInfo) {
            if (refundedPaid == null) {
                refundedPaid = new ArrayList<>();
            }
            refundedPaid.add(paidInfo);
            refundedTotal = refundedTotal.add(paidInfo.getSaleAmount());
        }

        Money refundedTotal() {
            return refundedTotal;
        }
    }
}
