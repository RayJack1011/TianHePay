package com.tianhe.pay.ui.order;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.devices.Printer;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.order.history.SyncCommand;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class OrderCountPresenter extends TianHePresenter<OrderCountContract.View>
    implements OrderCountContract.Presenter {

    @Inject
    Settings settings;
    private Global global;
    UseCase queryStatisticTask;
    private PrintUseCase printUseCase;
    private String mac;

    @Inject
    public OrderCountPresenter(Global global,
                               @Named("queryStatistic") UseCase queryStatisticTask,
                               PrintUseCase printUseCase,
                               @Named("wifiMac") String mac) {
        this.global = global;
        this.queryStatisticTask = queryStatisticTask;
        this.printUseCase = printUseCase;
        this.mac = mac;
    }

    @Override
    public void queryStatistic(Date date) {
        SyncCommand command = new SyncCommand(date.getTime(), mac);
        command.setIsPractice(settings.isTraining() ? "Y" : "N");
        queryStatisticTask.setReqParam(command);
        queryStatisticTask.execute(new DefaultObserver<OrderStatistics>(){
            @Override
            public void onNext(@NonNull OrderStatistics statistics) {
                Log.e("qqq","日结报表回参----->"+new Gson().toJson(statistics));
                view.renderStatistic(statistics);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.queryStatisticFail(e.getMessage());
            }
        });
    }

    @Override
    public void print(OrderStatistics statistics) {
        printUseCase.setReqParam(PrintUtils.orderStatistics(global, statistics));
        printUseCase.execute(new DefaultObserver<Printer.State>(){
            @Override
            public void onNext(Printer.State state) {
                if (state == Printer.State.NORMAL) {
                    view.printStatisticsSuccess();
                } else {
                    view.printStatisticsFail(state.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                view.printStatisticsFail(e.getMessage());
            }
        });
    }
}
