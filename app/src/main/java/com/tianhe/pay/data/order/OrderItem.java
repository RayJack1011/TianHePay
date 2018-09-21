package com.tianhe.pay.data.order;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.Skip;
import com.tianhe.pay.utils.money.Money;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String barcode;
    @Skip
    private String name;
    @SerializedName("num")
    private int quantity;
    private Money price;                // 实际单价
    private Money oldPrice;             // 原单价
    private Money saleAmount;           // 小记总售价
    @SerializedName("item")
    private int ordinal;                // 序号(从1开始)
    @SerializedName("oitem")
    private int oldOrdinal;             // 原序号(从1开始)
    private BigDecimal integral;        // 积分
    private Money handDiscount;        // 手工折扣
    private Money handDiscountInput;   // 手工折扣输入值

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Money getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Money oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Money getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Money saleAmount) {
        this.saleAmount = saleAmount;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOldOrdinal() {
        return oldOrdinal;
    }

    public void setOldOrdinal(int oldOrdinal) {
        this.oldOrdinal = oldOrdinal;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Money getSubtotal() {
        return oldPrice.multiply(new BigDecimal(quantity));
    }

    public Money getHandDiscount() {
        if (handDiscount == null) {
            return Money.zeroMoney();
        }
        return handDiscount;
    }

    public void setHandDiscount(Money handDiscount) {
        this.handDiscount = handDiscount;
    }

    public Money getHandDiscountInput() {
        if (handDiscountInput == null) {
            return Money.zeroMoney();
        }
        return handDiscountInput;
    }

    public void setHandDiscountInput(Money handDiscountInput) {
        this.handDiscountInput = handDiscountInput;
    }

    public Money getDiscountTotal() {
        if (saleAmount != null) {
            return getSubtotal().subtract(saleAmount);
        }
        return Money.zeroMoney();
    }

    public OrderItem copy() {
        OrderItem item = new OrderItem();
        item.setBarcode(barcode);
        item.setName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setOldPrice(oldPrice);
        item.setSaleAmount(saleAmount);
        item.setOrdinal(ordinal);
        item.setOldOrdinal(oldOrdinal);
        item.setIntegral(integral);
        item.setHandDiscount(handDiscount);
        item.setHandDiscountInput(handDiscountInput);
        return item;
    }
}
