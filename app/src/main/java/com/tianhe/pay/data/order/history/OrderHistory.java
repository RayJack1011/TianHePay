package com.tianhe.pay.data.order.history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 历史订单
 */
public class OrderHistory {
    @SerializedName("sale")
    private OrderHistoryHeader header;
    @SerializedName("saleDList")
    private List<OrderHistoryItem> items;
    @SerializedName("salePayList")
    private List<OrderHistoryPaid> paids;

    public OrderHistoryHeader getHeader() {
        return header;
    }

    public void setHeader(OrderHistoryHeader header) {
        this.header = header;
    }

    public List<OrderHistoryItem> getItems() {
        return items;
    }

    public void setItems(List<OrderHistoryItem> items) {
        this.items = items;
    }

    public List<OrderHistoryPaid> getPaids() {
        return paids;
    }

    public void setPaids(List<OrderHistoryPaid> paids) {
        this.paids = paids;
    }
}
