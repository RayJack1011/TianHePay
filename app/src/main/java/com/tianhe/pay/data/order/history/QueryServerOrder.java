package com.tianhe.pay.data.order.history;

public class QueryServerOrder {
    String saleNo;
    String isPactice;
    String shopNo;

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getIsPactice() {
        return isPactice;
    }

    public void setIsPactice(String isPactice) {
        this.isPactice = isPactice;
    }
}
