package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

import java.util.Date;
import java.util.List;

public interface OrderHistoryContract {
    interface View extends BaseView {

        void renderHistory(List<Order> orders);

        void queryHistoryFail(String reason);

        void syncHistorySuccess();

        void syncHistoryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void queryHistory();

        void syncHistory(Date date);
    }
}
