package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.history.QueryLocalCommand;
import com.tianhe.pay.data.order.history.SyncCommand;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class OrderHistoryPresenter extends TianHePresenter<OrderHistoryContract.View>
    implements OrderHistoryContract.Presenter {

    UseCase queryTask;
    UseCase syncTask;
    String mac;
    Settings settings;

    @Inject
    public OrderHistoryPresenter(@Named("queryLocalHistory") UseCase queryTask,
                                 @Named("syncTodayOrders") UseCase syncTask,
                                 @Named("wifiMac") String mac,
                                 Settings settings) {
        this.queryTask = queryTask;
        this.syncTask = syncTask;
        this.mac = mac;
        this.settings = settings;
    }

    @Override
    public void queryHistory() {
        QueryLocalCommand command = new QueryLocalCommand();
        command.setDate(new Date());
        command.setIsPractice(getIsPractice());
        queryTask.setReqParam(command);
        queryTask.execute(new DefaultObserver<List<Order>>(){
            @Override
            public void onNext(@NonNull List<Order> orders) {
                sortOrders(orders);
                view.renderHistory(orders);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.queryHistoryFail(e.getMessage());
            }
        });
    }

    private String getIsPractice() {
        return settings.isTraining() ? "Y" : "N";
    }

    private void sortOrders(List<Order> list) {
        Comparator<Order> comparator = new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                String sn1 = o1.getOrderHeader().getSaleNo();
                String sn2 = o2.getOrderHeader().getSaleNo();
                return sn2.compareTo(sn1);
            }
        };
        Collections.sort(list, comparator);
    }

    @Override
    public void syncHistory(Date date) {
        SyncCommand command = new SyncCommand(date.getTime(), mac);
        syncTask.setReqParam(command);//------->  SyncOrdersUseCase
        syncTask.execute(new DefaultObserver<List<Order>>() {
            @Override
            public void onNext(@NonNull List<Order> orders) {
                sortOrders(orders);
                view.renderHistory(orders);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.syncHistoryFail(e.getMessage());
            }
        });
    }
}
