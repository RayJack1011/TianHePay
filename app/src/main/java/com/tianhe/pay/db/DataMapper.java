package com.tianhe.pay.db;

import com.tianhe.pay.data.order.OrderHeader;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.utils.money.Money;

import java.util.ArrayList;
import java.util.List;

public class DataMapper {

    public static OrderHeaderEntity transform(OrderHeader header) {
        if (header == null) {
            return null;
        }
        OrderHeaderEntity entity = new OrderHeaderEntity();
        entity.setShopNo(header.getShopNo());
        entity.setSystemVersion(header.getSystemVersion());
        entity.setUserNo(header.getUserNo());
        entity.setTerminalId(header.getTerminalId());
        entity.setVipNo(header.getVipNo());
        entity.setVipLevel(header.getVipLevel());
        entity.setUuid(header.getUuid());
        entity.setSaleNo(header.getSaleNo());
        entity.setSaleNoSource(header.getSaleNoSource());
        entity.setTime(header.getTime());
        entity.setIsPractice(header.getIsPractice());
        return entity;
    }

    public static List<OrderItemEntity> transformItemList(List<OrderItem> items) {
        if (items == null) {
            return null;
        }
        List<OrderItemEntity> itemEntities = new ArrayList<>(items.size());
        for (OrderItem item : items) {
            OrderItemEntity entity = new OrderItemEntity();
            entity.setBarcode(item.getBarcode());
            entity.setName(item.getName());
            entity.setQuantity(item.getQuantity());
            if (item.getPrice() == null) {
                entity.setPrice(null);
            } else {
                entity.setPrice(item.getPrice().toString());
            }
            if (item.getOldPrice() == null) {
                entity.setOldPrice(null);
            } else {
                entity.setOldPrice(item.getOldPrice().toString());
            }
            if (item.getSaleAmount() == null) {
                entity.setSaleAmount(null);
            } else {
                entity.setSaleAmount(item.getSaleAmount().toString());
            }
            entity.setOrdinal(item.getOrdinal());
            entity.setOldOrdinal(item.getOldOrdinal());
            entity.setIntegral(item.getIntegral());
            if (item.getHandDiscount() == null) {
                entity.setHandDiscount(null);
            } else {
                entity.setHandDiscount(item.getHandDiscount().toString());
            }
            if (item.getHandDiscountInput() == null) {
                entity.setHandDiscountInput(null);
            } else {
                entity.setHandDiscountInput(item.getHandDiscountInput().toString());
            }
            if (item.getHandDiscountInput() == null) {
                entity.setHandDiscountInput(null);
            } else {
                entity.setHandDiscountInput(item.getHandDiscountInput().toString());
            }
            itemEntities.add(entity);
        }
        return itemEntities;
    }

    public static List<PaidInfoEntity> transformPaidList(List<PaidInfo> paidInfos) {
        if (paidInfos == null) {
            return null;
        }
        List<PaidInfoEntity> paidInfoEntities = new ArrayList<>(paidInfos.size());
        for (PaidInfo paidInfo : paidInfos) {
            PaidInfoEntity entity = new PaidInfoEntity();
            entity.setPaymentId(paidInfo.getPaymentId());
            entity.setPaymentName(paidInfo.getPaymentName());
            entity.setRelationNumber(paidInfo.getBillNo());
            if (paidInfo.getSaleAmount() == null) {
                entity.setSaleAmount(null);
            } else {
                entity.setSaleAmount(paidInfo.getSaleAmount().toString());
            }
            entity.setTime(paidInfo.getTime());
            paidInfoEntities.add(entity);
        }
        return paidInfoEntities;
    }

    public static OrderHeader transform(OrderHeaderEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderHeader header = new OrderHeader();
        header.setShopNo(entity.getShopNo());
        header.setSystemVersion(entity.getSystemVersion());
        header.setUserNo(entity.getUserNo());
        header.setTerminalId(entity.getTerminalId());
        header.setVipNo(entity.getVipNo());
        header.setVipLevel(entity.getVipLevel());
        header.setUuid(entity.getUuid());
        header.setSaleNo(entity.getSaleNo());
        header.setSaleNoSource(entity.getSaleNoSource());
        header.setTime(entity.getTime());
        header.setRefunded(entity.isRefunded());
        header.setIsPractice(entity.getIsPractice());
        return header;
    }

    public static OrderItem transform(OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        item.setBarcode(entity.getBarcode());
        item.setName(entity.getName());
        item.setQuantity(entity.getQuantity());
        if (entity.getPrice() == null) {
            item.setPrice(null);
        } else {
            item.setPrice(Money.createAsYuan(entity.getPrice()));
        }
        if (entity.getOldPrice() == null) {
            item.setOldPrice(null);
        } else {
            item.setOldPrice(Money.createAsYuan(entity.getOldPrice()));
        }
        if (entity.getSaleAmount() == null) {
            item.setSaleAmount(null);
        } else {
            item.setSaleAmount(Money.createAsYuan(entity.getSaleAmount()));
        }
        item.setOrdinal(entity.getOrdinal());
        item.setOldOrdinal(entity.getOldOrdinal());
        item.setIntegral(entity.getIntegral());
        if (entity.getHandDiscount() == null) {
            item.setHandDiscount(null);
        } else {
            item.setHandDiscount(Money.createAsYuan(entity.getHandDiscount()));
        }
        if (entity.getHandDiscountInput() == null) {
            item.setHandDiscountInput(null);
        } else {
            item.setHandDiscountInput(Money.createAsYuan(entity.getHandDiscountInput()));
        }
        return item;
    }

    public static PaidInfo transform(PaidInfoEntity entity) {
        if (entity == null) {
            return null;
        }
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(entity.getPaymentId());
        paidInfo.setPaymentName(entity.getPaymentName());
        paidInfo.setBillNo(entity.getRelationNumber());
        if (entity.getSaleAmount() == null) {
            paidInfo.setSaleAmount(null);
        } else {
            paidInfo.setSaleAmount(Money.createAsYuan(entity.getSaleAmount()));
        }
        paidInfo.setTime(entity.getTime());
        return paidInfo;
    }
}
