package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.ui.setting.Settings;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class OrderQueryPresenter extends TianHePresenter<OrderQueryContract.View>
    implements OrderQueryContract.Presenter {

    UseCase queryTask;
    @Inject
    Settings settings;

    @Inject
    public OrderQueryPresenter(@Named("queryServerHistory") UseCase queryTask) {
        this.queryTask = queryTask;
    }

    public void queryOrderBySaleNo(String saleNo) {
        queryTask.setReqParam(saleNo);
        queryTask.execute(new DefaultObserver<Order>() {
            @Override
            public void onNext(@NonNull Order order) {
                boolean isTraining = settings.isTraining();
                String isPractice = order.getOrderHeader().getIsPractice();
                boolean sameMode = isTraining & ("Y".equals(isPractice));
//                if (sameMode) {
                    view.querySuccess(order);
//                } else {
//                    String errorMsg = "模式不匹配! 订单不是";
//                    if (isTraining) {
//                        view.queryHistoryFail(errorMsg + "练习模式");
//                    } else {
//                        view.queryHistoryFail(errorMsg + "正常销售模式");
//                    }
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.queryHistoryFail(e.getMessage());
            }
        });
    }
}
