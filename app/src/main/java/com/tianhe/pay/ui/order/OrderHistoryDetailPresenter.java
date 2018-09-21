package com.tianhe.pay.ui.order;

import com.tianhe.devices.Printer;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class OrderHistoryDetailPresenter extends TianHePresenter<OrderHistoryDetailContract.View>
        implements OrderHistoryDetailContract.Presenter {

    PrintUseCase printTask;
    Global global;

    @Inject
    public OrderHistoryDetailPresenter(PrintUseCase printTask,
                                       Global global) {
        this.printTask = printTask;
        this.global = global;
    }

    @Override
    public void reprintOrder(Order order) {
        printTask.setReqParam(PrintUtils.billOrder(order, true, global.getSupplierName()));
        printTask.execute(new DefaultObserver<Printer.State>() {
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.reprintOrderFail(state.getMessage());
                } else {
                    view.reprintOrderSuccess();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.reprintOrderFail(e.getMessage());
            }
        });
    }
}
