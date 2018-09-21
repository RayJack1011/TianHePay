package com.tianhe.pay.data.order.history;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;

/**
 * 历史订单中的商品项
 */
public class OrderHistoryItem {
    @SerializedName("PLUNO")
    String barcode;
    @SerializedName("PLUNAME")          // 商品名称
    String name;
    @SerializedName("PRICE")
    Money price;                        // 销售单价
    @SerializedName("OLDPRICE")
    Money oldPrice;                     // 原单价
    @SerializedName("COUNTERAMT")
    Money saleAmount;                   // 总销售金额
    @SerializedName("ITEM")
    private int ordinal;                // 序号(从1开始)
    @SerializedName("OITEM")
    private int oldOrdinal;
    @SerializedName("QTY")
    private int quantity;               // 数量
    @SerializedName("DISC")
    Money discountTotal;                // 折扣总额
    @SerializedName("POINT_QTY")
    private BigDecimal integral;        // 积分

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(Money discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }
}
