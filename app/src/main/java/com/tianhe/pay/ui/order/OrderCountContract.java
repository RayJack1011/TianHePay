package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

import java.util.Date;

public interface OrderCountContract {
    interface View extends BaseView {
        void printStatisticsSuccess();

        void printStatisticsFail(String reason);

        void renderStatistic(OrderStatistics statistics);

        void queryStatisticFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void queryStatistic(Date date);

        void print(OrderStatistics statistics);
    }
}
