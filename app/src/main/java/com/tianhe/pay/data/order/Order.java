package com.tianhe.pay.data.order;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("sale")
    private OrderHeader orderHeader;
    @SerializedName("saleD")
    private List<OrderItem> orderItems;
    @SerializedName("salePay")
    private List<PaidInfo> paidInfos;

    transient Money adjustedTotal = null;
    transient Money total = null;

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<PaidInfo> getPaidInfos() {
        return paidInfos;
    }

    public void setPaidInfos(List<PaidInfo> paidInfos) {
        this.paidInfos = paidInfos;
    }

    public void setVip(Member member) {
        if (member == null) {
            orderHeader.setVipNo(null);
            orderHeader.setVipLevel(null);
        } else {
            orderHeader.setVipNo(member.getVipNo());
            orderHeader.setVipLevel(member.getLevel());
        }
    }

    public void clear() {
        this.adjustedTotal = null;
        this.total = null;
        this.orderHeader = null;
        if (this.orderItems != null) {
            this.orderItems.clear();
        }
        if (this.paidInfos != null) {
            this.paidInfos.clear();
        }
    }

    public int getCount() {
        if (orderItems == null || orderItems.size() == 0) {
            return 0;
        }
        int count = 0;
        for (OrderItem item : orderItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public boolean isEmpty() {
        return orderItems == null || orderItems.size() == 0;
    }

    public void addItem(OrderItem item) {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        if (item.getOrdinal() == 0) {
            item.setOrdinal(orderItems.size() + 1);
        }
        orderItems.add(item);
        if (total == null) {
            total = Money.zeroMoney();
        }
        total = total.add(item.getSubtotal());
    }

    public void adjustItem(OrderItem adjustedItem) {
        boolean modifyItem = false;
        for (OrderItem item : this.orderItems) {
            if (equalItem(item, adjustedItem)) {
                item.setPrice(adjustedItem.getPrice());
                item.setQuantity(adjustedItem.getQuantity());
                item.setSaleAmount(adjustedItem.getSaleAmount());
                item.setIntegral(adjustedItem.getIntegral());
                item.setHandDiscount(adjustedItem.getHandDiscount());
                item.setHandDiscountInput(adjustedItem.getHandDiscountInput());
                modifyItem = true;
                break;
            }
        }
        if (modifyItem) {
            total = calculateTotal();
        } else {
            addItem(adjustedItem);
        }
    }

    public OrderItem getItemByIndex(int index) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.get(index);
    }

    public void removeItem(int itemIndex) {
        orderItems.remove(itemIndex);
        // 修改序号
        for (int i = itemIndex; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            item.setOrdinal(i + 1);
        }
        total = calculateTotal();
    }

    public void adjustItems(List<OrderItem> adjustedItems) {
        for (OrderItem item : this.orderItems) {
            for (OrderItem adjusted : adjustedItems) {
                if (equalItem(item, adjusted)) {
                    item.setPrice(adjusted.getPrice());
                    item.setSaleAmount(adjusted.getSaleAmount());
                    item.setIntegral(adjusted.getIntegral());
                }
            }
        }
    }

    public Money getTotal() {
        if (total != null) {
            return total;
        }
        total = calculateTotal();
        return total;
    }

    private Money calculateTotal() {
        Money total = Money.zeroMoney();
        if (orderItems == null) {
            return total;
        }
        for (OrderItem item : orderItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * @return 计算折扣后的订单总额
     */
    public Money getAdjustedTotal() {
//        if (adjustedTotal != null) {
//            return adjustedTotal;
//        }
        adjustedTotal = Money.zeroMoney();
        if (orderItems == null) {
            return adjustedTotal;
        }
        for (OrderItem item : orderItems) {
            if (item.getSaleAmount() != null) {
                adjustedTotal = adjustedTotal.add(item.getSaleAmount());
            }
        }
        return adjustedTotal;
    }

    public Money getDiscountTotal() {
        return getTotal().subtract(getAdjustedTotal());
    }

    /**
     * @return 订单总积分
     */
    public BigDecimal getPointTotal() {
        BigDecimal pointTotal = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            if (item.getIntegral() != null) {
                pointTotal = pointTotal.add(item.getIntegral());
            }
        }
        return pointTotal;
    }

    /**
     * @return true, 退货单; false, 销售单
     */
    public boolean isRefund() {
        return orderHeader.getSaleNoSource() != null;
    }

    public boolean isRefunded() {
        return orderHeader.isRefunded();
    }

    public String getSaleNoSource() {
        return orderHeader.getSaleNoSource();
    }

    private static boolean equalItem(OrderItem first, OrderItem second) {
        return first.getBarcode().equals(second.getBarcode())
                && (first.getOrdinal() == second.getOrdinal());
    }

}
