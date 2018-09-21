package com.tianhe.pay.model;

import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.tianhe.pay.data.order.calculate.Gift;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.crm.payment.CouponProcess;
import com.tianhe.pay.data.crm.payment.StoredValueCardPay;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.data.goods.Goods;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderHeader;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

import static com.tianhe.pay.model.CartManager.Event.CART_ITEM_CHANGED;
import static com.tianhe.pay.model.CartManager.Event.ORDER_PAID_CHANGED;
import static com.tianhe.pay.model.CartManager.Event.VIP_CHANGED;

public class CartManager {

    public static class Event {
        public static final String VIP_CHANGED = "vipChanged";
        public static final String CART_ITEM_CHANGED = "itemChanged";
        public static final String ORDER_PAID_CHANGED = "paidChanged";
    }

//    private transient BehaviorRelay<Member> vipChanged = BehaviorRelay.create();
//    private transient BehaviorRelay<Double> paidTotal = BehaviorRelay.createDefault(0D);
    private transient BehaviorRelay<String> cartChanged = BehaviorRelay.create();

    private KeypadItem keypadItem;
    private Order order;

    private Global global;
    private String version;
    private SensitiveValues sensitiveValues;
    private boolean inPosMode;

    public CartManager(Global global, String version) {
        this.global = global;
        this.version = version;
        keypadItem = new KeypadItem();
        sensitiveValues = new SensitiveValues();
        order = new Order();
        init();
    }

    private void init() {
        OrderHeader header = new OrderHeader();
        header.setShopNo(global.getShopNo());
        header.setTerminalId(global.getTerminalId());
        header.setUserNo(global.getUserNo());
        header.setSystemVersion(version);
        header.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderHeader(header);
    }

    // region keypad Methods

    public void setKeypadPrice(long price) {
        if (keypadItem.getPriceCent() != price) {
            keypadItem.setPriceCent(price);
        }
    }

    public void setKeypadQuantity(int quantity) {
        keypadItem.setQuantity(quantity);
    }

    public void setKeypadhandDiscount(long quantity) {
        keypadItem.setHandDiscount(quantity);
    }

    public void setKeypadhandDiscountInput(long quantity) {
        keypadItem.setHandDiscountInput(quantity);
    }

    public long getKeypadPrice() {
        return keypadItem.getPriceCent();
    }

    public void setKeypadGoods(Goods goods) {
        if (goods != null && !goods.equals(keypadItem.getGoods())) {
            keypadItem.setGoods(goods);
        }
    }

    public Order forCalculateKeypadItem() {
        Order itemOrder = new Order();
        OrderHeader header = order.getOrderHeader().copy();
        header.setUuid(null);
        itemOrder.setOrderHeader(header);
        itemOrder.addItem(keypadItem.orderItem());
        return itemOrder;
    }

    public void onCalculatedKeypadItem(CalculationResult result) {
        OrderItem adjustedItem = result.getAdjustedItems().get(0);
        OrderItem newItem = keypadItem.orderItem();
        newItem.setPrice(adjustedItem.getPrice());
        newItem.setSaleAmount(adjustedItem.getSaleAmount());
        order.addItem(newItem);
        keypadItem.reset();
        notifyItemChanged();
    }

    // endregion keypad Methods

    // region vip Methods

    public void setVip(Member member) {
        if (member == null) {
            sensitiveValues.vip = Member.NONE;
        } else {
            sensitiveValues.vip = member;
        }
        if (sensitiveValues.vip == Member.NONE) {
            order.setVip(null);
        } else {
            order.setVip(sensitiveValues.vip);
        }
        cartChanged.accept(VIP_CHANGED);
    }

    public Member currentVip() {
        Member member = sensitiveValues.vip;
        return member == null ? Member.NONE : member;
    }

    // endregion vip Methods

    // region item Methods

    public void removeItem(int itemIndex) {
        order.removeItem(itemIndex);
        notifyItemChanged();
    }

    public Order forCalculateModifyItem(int index, int quantity, Money money) {
        Order itemOrder = new Order();
        OrderHeader header = order.getOrderHeader().copy();
        header.setUuid(null);
        itemOrder.setOrderHeader(header);

        OrderItem oriItem = order.getItemByIndex(index);
        OrderItem item = oriItem.copy();
        item.setQuantity(quantity);
        item.setPrice(money);
        item.setHandDiscountInput(money);
        Money saleAmount = money.multiply(new BigDecimal(quantity));
        item.setSaleAmount(saleAmount);
        item.setHandDiscount(item.getSubtotal().subtract(saleAmount));
        itemOrder.addItem(item);
        return itemOrder;
    }

    public void onCalculatedModifyItem(CalculationResult result) {
        order.adjustItem(result.getAdjustedItems().get(0));
        notifyItemChanged();
    }

    // endregion item Methods

    // region calculate Methods

    /**
     * 用于计算整单价格
     */
    public Order forCalculate() {
        return order;
    }

    public void onCalculated(CalculationResult calculationResult) {
        sensitiveValues.calculationResult = calculationResult;
        order.adjustItems(calculationResult.getAdjustedItems());
    }

    // endregion calculate Methods

    // region pay Methods

    public void setSafeSaleNo(String saleNo) {
        this.sensitiveValues.saleNo = saleNo;
        order.getOrderHeader().setSaleNo(saleNo);
    }

    public String getSafeSaleNo() {
        return this.sensitiveValues.saleNo;
    }

    public boolean canUseCardPay(StoredValueCard card) {
        if (!isCalculatedOrder()) {
            throw new IllegalStateException("check use StoredValueCard must after calculate Order");
        }
        return sensitiveValues.canUseStoredValueCard(card);
    }

    public boolean canUseCoupon(Coupon coupon) {
        if (!isCalculatedOrder()) {
            throw new IllegalStateException("must check use StoredValueCard must after calculate Order");
        }
        return sensitiveValues.canUseCoupon(coupon);
    }

    public void addPaidInfo(PaidInfo paidInfo) {
        sensitiveValues.addPaidInfo(paidInfo);
        cartChanged.accept(ORDER_PAID_CHANGED);
    }

    // endregion pay Methods

    // region submit bill

    public Order forSubmitOrder() {
        if (sensitiveValues.paidInfos != null) {
            order.setPaidInfos(sensitiveValues.paidInfos);
        }
        return order;
    }

    public void submitOrderSuccess(SubmitOrderResult result) {
        if (order != null) {
            order.getOrderHeader().setTime(result.getOpTime());
        }
    }

    // endregion submit bill

    // region cart state Methods

    public void clear() {
        keypadItem.reset();
        sensitiveValues.reset();
        this.order.clear();
        init();  // 设置order header
        notifyItemChanged();
    }

    public Money getSubTotal() {
        return order.getTotal();
    }

    public int getSaleCount() {
        return order.getCount();
    }

    public boolean isCalculatedOrder() {
        return sensitiveValues.calculationResult != null;
    }

    public Money getAdjustedTotal() {
//        if (!isCalculatedOrder()) {
//            throw new IllegalStateException("has not calculate Order, must invoke server calculate before " +
//                    "getAdjustedTotal");
//        }
        return order.getAdjustedTotal();
    }

    public boolean hasVip() {
        return sensitiveValues.vip != null && !sensitiveValues.vip.equals(Member.NONE);
    }

    public OrderItem getOrderItemByIndex(int index) {
        return order.getItemByIndex(index);
    }

    public BigDecimal getPointTotal() {
        return order.getPointTotal();
    }

    public Money getPaidTotal() {
        if (!isCalculatedOrder()) {
            throw new IllegalStateException("has not calculate Order, must invoke server calculate before");
        }
        return sensitiveValues.paidTotal();
    }

    /**
     * 是否处于补录阶段.
     * 天和的补录有专门的补录支付方式, 不需要判断是否为补录模式
     */
    @Deprecated
    public boolean inPosMode() {
        return inPosMode;
    }

    @Deprecated
    public void changeMode(boolean inPosMode) {
        this.inPosMode = inPosMode;
    }


    public boolean hasAliPaid(){
        if (sensitiveValues.paidInfos == null) {
            return false;
        }
        for (PaidInfo paidInfo : sensitiveValues.paidInfos) {
            String paidId = paidInfo.getPaymentId();
            if (paidId.equals(PaymentSignpost.ALI.getPaymentId())) {
                return true;
            }
        }
        return false;
    }
    public  boolean hasWechatPaid(){
        if (sensitiveValues.paidInfos == null) {
            return false;
        }
        for (PaidInfo paidInfo : sensitiveValues.paidInfos) {
            String paidId = paidInfo.getPaymentId();
            if (paidId.equals(PaymentSignpost.WECHAT.getPaymentId())) {
                return true;
            }
        }
        return false;
    }
    public boolean hasWechatAliPaid() {
        if (sensitiveValues.paidInfos == null) {
            return false;
        }
        for (PaidInfo paidInfo : sensitiveValues.paidInfos) {
            String paidId = paidInfo.getPaymentId();
            if (paidId.equals(PaymentSignpost.ALI.getPaymentId()) ||
                    paidId.equals(PaymentSignpost.WECHAT.getPaymentId())) {
                return true;
            }
        }
        return false;
    }

    public List<CouponProcess> getCouponPays() {
        List<CouponProcess> couponProcesses = new ArrayList<>();
        for (PaidInfo paidInfo : sensitiveValues.paidInfos) {
            if (paidInfo.getPaymentId().equals(PaymentSignpost.COUPON.getPaymentId())) {
                CouponProcess couponProcess = new CouponProcess();
                couponProcess.setStartCouponNo(paidInfo.getBillNo());
                couponProcess.setEndCouponNo(paidInfo.getBillNo());
                couponProcess.setUsedState();
                couponProcess.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                couponProcesses.add(couponProcess);
            }
        }
        return couponProcesses;
    }

    public List<StoredValueCardPay> getStoredValuePays() {
        List<StoredValueCardPay> storedValueCardPays = new ArrayList<>();
        for (PaidInfo paidInfo : sensitiveValues.paidInfos) {
            if (paidInfo.getPaymentId().equals(PaymentSignpost.STOREDVALUE_CARD.getPaymentId())) {
                StoredValueCardPay pay = new StoredValueCardPay();
                pay.setCardNo(paidInfo.getBillNo());
                pay.setAmount(paidInfo.getSaleAmount().toString());
                pay.setPassword(paidInfo.getPwd());
                pay.setVerifyCode(paidInfo.getMarkCode());
                pay.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                storedValueCardPays.add(pay);
            }
        }
        return storedValueCardPays;
    }

    public List<Gift> getCouponGift() {
        return sensitiveValues.getCouponGifts();
    }

    /** 是否包含赠券 */
    public boolean hasCouponGift() {
        return sensitiveValues.getCouponGifts() != null
                && sensitiveValues.getCouponGifts().size() > 0;

    }
    public List<Gift> getCouponList(){
        return  sensitiveValues.calculationResult.getCouponList();
    }
    public boolean hasCouponList(){
        Log.e("qqq",new Gson().toJson(sensitiveValues.calculationResult.getCouponList())+":::::::;;heheda");
        return  sensitiveValues.calculationResult.getCouponList() != null
                && sensitiveValues.calculationResult.getCouponList().size()>0;
    }

    public boolean isEmpty() {
        return order.isEmpty();
    }

    public OrderItem getLastItem() {
        if (isEmpty()) {
            return null;
        }
        List<OrderItem> items = order.getOrderItems();
        return items.get(items.size() - 1);
    }

    public int getCartSize() {
        if (order.getOrderItems() == null) {
            return 0;
        }
        return order.getOrderItems().size();
    }

    public List<OrderItem> getCartItems() {
        if (order.getOrderItems() == null) {
            return null;
        }
        return Collections.unmodifiableList(order.getOrderItems());
    }

    public boolean hasPaidInfo() {
        if (!isCalculatedOrder()) {
            throw new IllegalStateException("has not calculate Order, all PaidInfo is invalid");
        }
        return sensitiveValues.paidInfos != null && !sensitiveValues.paidInfos.isEmpty();
    }

    public Money getPendingAmount() {
        if (!isCalculatedOrder()) {
            throw new IllegalStateException("has not calculate Order, all PaidInfo is invalid");
        }
        return getAdjustedTotal().subtract(getPaidTotal());
    }

    public boolean hasRemainPendingAmount() {
        return getAdjustedTotal().getAmount() - getPaidTotal().getAmount() > 0;
    }

    public Observable<String> cartChanged() {
        return cartChanged;
    }

    private void notifyItemChanged() {
        cartChanged.accept(CART_ITEM_CHANGED);
    }

    // endregion cart state Methods

    private static class SensitiveValues {
        Member vip;
        String saleNo;
        CalculationResult calculationResult;
        List<PaidInfo> paidInfos;
        private Money paidTotal;

        public SensitiveValues() {
            paidTotal = Money.zeroMoney();
        }

        void reset() {
            vip = null;
            saleNo = null;
            calculationResult = null;
            paidInfos = null;
            paidTotal = Money.zeroMoney();
        }

        boolean canUseStoredValueCard(StoredValueCard card) {
            if (calculationResult.getLimitCard() == null) {
                return true;
            }
            return calculationResult.getLimitCard().canUseStoredValueCard(card);
        }

        boolean canUseCoupon(Coupon coupon) {
            if (calculationResult.getLimitCoupon() == null) {
                return true;
            }
            return true;
        }

        List<Gift> getCouponGifts() {
            return calculationResult.getCouponList();
        }

        void addPaidInfo(PaidInfo paidInfo) {
            if (paidInfos == null) {
                paidInfos = new ArrayList<>();
            }
            paidInfos.add(paidInfo);
            paidTotal = paidTotal.add(paidInfo.getSaleAmount());
        }

        Money paidTotal() {
            return paidTotal;
        }
    }

    public static class KeypadItem {
        private Goods goods;
        private int quantity;
        private long priceCent;
        private long handDiscount;
        private long handDiscountInput;

        public void setHandDiscount(long handDiscount) {
            this.handDiscount = handDiscount;
        }

        public void setHandDiscountInput(long handDiscountInput) {
            this.handDiscountInput = handDiscountInput;
        }

        public long getHandDiscount() {
            return handDiscount;
        }

        public long getHandDiscountInput() {
            return handDiscountInput;
        }

        public KeypadItem() {
            this.quantity = 1;
        }

        public Goods getGoods() {
            return goods;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }

        public long getPriceCent() {
            return priceCent;
        }

        public void setPriceCent(long priceCent) {
            this.priceCent = priceCent;
        }

        public void reset() {
            goods = null;
            priceCent = 0;
            quantity = 1;
        }

        OrderItem orderItem() {
            OrderItem orderItem = new OrderItem();
            orderItem.setOldPrice(new Money(priceCent));
            orderItem.setQuantity(quantity);
            orderItem.setBarcode(goods.getBarcode());
            orderItem.setName(goods.getName());
            orderItem.setHandDiscount(new Money(handDiscount));
            orderItem.setHandDiscountInput(new Money(handDiscountInput));
            return orderItem;
        }

    }
}
