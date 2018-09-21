package com.tianhe.pay.db;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.history.QueryLocalCommand;

import java.util.Date;
import java.util.List;

public interface DbCache {
    /** 根据营业日期删减不需要的订单记录 */
    void reduceOrders(Date date) throws Exception;

    List<Order> queryOrderHistory(QueryLocalCommand command) throws Exception;

    void saveOrder(Order order) throws Exception;
}
