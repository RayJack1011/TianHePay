package com.tianhe.pay.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;

@DatabaseTable
public class OrderItemEntity {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String barcode;
    @DatabaseField
    private String name;
    @DatabaseField
    private int quantity;
    @DatabaseField
    private String price;                // 实际单价
    @DatabaseField
    private String oldPrice;             // 原单价
    @DatabaseField
    private String saleAmount;           // 小记总售价
    @DatabaseField
    private int ordinal;                // 序号(从1开始)
    @DatabaseField
    private int oldOrdinal;             // 原序号(从1开始)
    @DatabaseField
    private BigDecimal integral;        // 积分
    @DatabaseField
    private String handDiscount;         // 手工折扣
    @DatabaseField
    private String handDiscountInput;    // 手工折扣输入值
    @DatabaseField
    private String businessDate;        // 所属日期


    @DatabaseField(foreign = true, foreignColumnName = "saleNo")
    private OrderHeaderEntity head;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
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

    public String getHandDiscount() {
        return handDiscount;
    }

    public void setHandDiscount(String handDiscount) {
        this.handDiscount = handDiscount;
    }

    public String getHandDiscountInput() {
        return handDiscountInput;
    }

    public void setHandDiscountInput(String handDiscountInput) {
        this.handDiscountInput = handDiscountInput;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public OrderHeaderEntity getHead() {
        return head;
    }

    public void setHead(OrderHeaderEntity head) {
        this.head = head;
    }
}
