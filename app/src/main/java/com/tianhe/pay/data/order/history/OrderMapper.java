package com.tianhe.pay.data.order.history;

import android.support.annotation.NonNull;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderHeader;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.payment.PaidInfo;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static Order mapper(OrderHistory history) {
        if (history == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderHeader(mapperHeader(history.getHeader()));

        List<OrderHistoryItem> historyItems = history.getItems();
        if (historyItems != null && historyItems.size() > 0) {
            List<OrderItem> orderItems = new ArrayList<>(historyItems.size());
            for (OrderHistoryItem historyItem : historyItems) {
                orderItems.add(mapperItem(historyItem));
            }
            order.setOrderItems(orderItems);
        }

        List<OrderHistoryPaid> historyPaids = history.getPaids();
        if (historyPaids != null && historyPaids.size() > 0) {
            List<PaidInfo> paidInfos = new ArrayList<>(historyPaids.size());
            for (OrderHistoryPaid historyPaid : historyPaids) {
                paidInfos.add(mapperPaidInfo(historyPaid));
            }
            order.setPaidInfos(paidInfos);
        }
        return order;
    }

    @NonNull
    private static PaidInfo mapperPaidInfo(OrderHistoryPaid historyPaid) {
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setSaleAmount(historyPaid.getSaleAmount());
        paidInfo.setBillNo(historyPaid.getRelationNumber());
        paidInfo.setPaymentId(historyPaid.getPaymentId());
        paidInfo.setPaymentName(historyPaid.getPaymentName());
        return paidInfo;
    }

    @NonNull
    private static OrderItem mapperItem(OrderHistoryItem historyItem) {
        OrderItem item = new OrderItem();
        item.setBarcode(historyItem.getBarcode());
        item.setName(historyItem.getName());
        item.setPrice(historyItem.getPrice());
        item.setSaleAmount(historyItem.getSaleAmount());
        item.setOldPrice(historyItem.getOldPrice());
        item.setQuantity(historyItem.getQuantity());
        item.setOrdinal(historyItem.getOrdinal());
        item.setOldOrdinal(historyItem.getOldOrdinal());
        item.setIntegral(historyItem.getIntegral());
        return item;
    }

    @NonNull
    private static OrderHeader mapperHeader(OrderHistoryHeader historyHeader) {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setSaleNo(historyHeader.getSaleNo());
        orderHeader.setSaleNoSource(historyHeader.getSaleNoSource());
        orderHeader.setShopNo(historyHeader.getShopNo());
        orderHeader.setSystemVersion(historyHeader.getSystemVersion());
        orderHeader.setTime(historyHeader.getDate() + " " + historyHeader.getTime());
        orderHeader.setTerminalId(historyHeader.getTerminalId());
        orderHeader.setVipNo(historyHeader.getVipNo());
        orderHeader.setUserNo(historyHeader.getUserNo());
        orderHeader.setIsPractice(historyHeader.getIsPractice());
        return orderHeader;
    }
}
