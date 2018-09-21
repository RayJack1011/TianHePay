package com.tianhe.pay.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.order.history.QueryLocalCommand;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class DbCacheImpl implements DbCache {

    DatabaseHelper dbHelper;

    public DbCacheImpl(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void reduceOrders(Date date) throws Exception {
        String target = Times.yyyyMMdd(date);
        String targetYesterday = Times.yyyyMMdd(Times.getRelativeDate(date, -1));
        Dao<OrderHeaderEntity, String> headerDao = dbHelper.getDao(OrderHeaderEntity.class);
        DeleteBuilder<OrderHeaderEntity, String> headerDeleteBuilder = headerDao.deleteBuilder();
        headerDeleteBuilder.where().notIn("businessDate", target, targetYesterday);
        headerDeleteBuilder.delete();
        Dao<OrderItemEntity, Long> itemDao = dbHelper.getDao(OrderItemEntity.class);
        DeleteBuilder<OrderItemEntity, Long> itemDeleteBuilder = itemDao.deleteBuilder();
        itemDeleteBuilder.where().notIn("businessDate", target, targetYesterday);
        itemDeleteBuilder.delete();
        Dao<PaidInfoEntity, Long> paidDao = dbHelper.getDao(PaidInfoEntity.class);
        DeleteBuilder<PaidInfoEntity, Long> paidDeleteBuilder = paidDao.deleteBuilder();
        paidDeleteBuilder.where().notIn("businessDate", target, targetYesterday);
        paidDeleteBuilder.delete();
    }

    @Override
    public List<Order> queryOrderHistory(QueryLocalCommand command) throws Exception {
        Dao<OrderHeaderEntity, String> headerDao = dbHelper.getDao(OrderHeaderEntity.class);
        List<OrderHeaderEntity> headerEntities;
        if (command.getDate() != null) {
            QueryBuilder<OrderHeaderEntity, String> queryBuilder = headerDao.queryBuilder();
            if (Strings.isNotEmpty(command.getIsPractice())) {
                queryBuilder.where()
                        .eq("businessDate", Times.yyyyMMdd(command.getDate()))
                        .and()
                        .eq("isPractice", command.getIsPractice());
            } else {
                queryBuilder.where()
                        .eq("businessDate", Times.yyyyMMdd(command.getDate()));
            }
            headerEntities = queryBuilder.query();
        } else {
            headerEntities = headerDao.queryForAll();
        }
        List<Order> orders = new ArrayList<>(headerEntities.size());
        for (OrderHeaderEntity headerEntity : headerEntities) {
            Order order = new Order();
            order.setOrderHeader(DataMapper.transform(headerEntity));
            // item
            Collection<OrderItemEntity> itemEntities = headerEntity.getDetails();
            List<OrderItem> items = new ArrayList<>();
            for (OrderItemEntity entity : itemEntities) {
                items.add(DataMapper.transform(entity));
            }
            order.setOrderItems(items);
            // paid info
            Collection<PaidInfoEntity> paidInfoEntities = headerEntity.getPayInfos();
            List<PaidInfo> paidInfos = new ArrayList<>();
            for (PaidInfoEntity entity : paidInfoEntities) {
                if (entity.getPaymentId().equals("8005")) {
                    entity.setPaymentId("03");
                }
                if (entity.getPaymentId().equals("8007")) {
                    entity.setPaymentId("02");
                }
                if (entity.getPaymentId().equals("5006")) {
                    entity.setPaymentId("04");
                }
                if (entity.getPaymentId().equals("9004")) {
                    entity.setPaymentId("07");
                }
                if (entity.getPaymentId().equals("80070")) {
                    entity.setPaymentId("05");
                }
                if (entity.getPaymentId().equals("5099")) {
                    entity.setPaymentId("06");
                }
                //add by hujie 2018-05-29 ----start
                if (entity.getPaymentId().equals("8098")) {
                    entity.setPaymentId("05");
                }
                if (entity.getPaymentId().equals("5065")) {
                    entity.setPaymentId("06");
                }
                if (entity.getPaymentId().equals("8099")) {
                    entity.setPaymentId("07");
                }
                //add by hujie 2018-05-29 ----end

                paidInfos.add(DataMapper.transform(entity));

            }
            order.setPaidInfos(paidInfos);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public void saveOrder(Order order) throws Exception {
        final String businessDate = order.getOrderHeader().getTime().substring(0, 8);
        Dao<OrderHeaderEntity, String> headerDao = dbHelper.getDao(OrderHeaderEntity.class);
        final OrderHeaderEntity headerEntity = DataMapper.transform(order.getOrderHeader());
        headerEntity.setBusinessDate(businessDate);
        int count = headerDao.create(headerEntity);
        final Dao<OrderItemEntity, Long> itemDao = dbHelper.getDao(OrderItemEntity.class);
        final List<OrderItemEntity> itemEntities = DataMapper.transformItemList(order.getOrderItems());
        itemDao.callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (OrderItemEntity entity : itemEntities) {
                    entity.setBusinessDate(businessDate);
                    entity.setHead(headerEntity);
                    itemDao.create(entity);
                }
                return null;
            }
        });
        final Dao<PaidInfoEntity, Long> paidDao = dbHelper.getDao(PaidInfoEntity.class);
        final List<PaidInfoEntity> paidEntities = DataMapper.transformPaidList(order.getPaidInfos());
        paidDao.callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (PaidInfoEntity entity : paidEntities) {
                    entity.setBusinessDate(businessDate);
                    entity.setHead(headerEntity);
                    paidDao.create(entity);
                }
                return null;
            }
        });
        if (order.isRefund()) {
            UpdateBuilder<OrderHeaderEntity, String> updateBuilder = headerDao.updateBuilder();
            updateBuilder.updateColumnValue("refunded", true)
                    .where().eq("saleNo", order.getSaleNoSource());
            int updateCount = updateBuilder.update();
            if (updateCount <= 0) {
                // 没有更新数据
            }
        }
    }
}
