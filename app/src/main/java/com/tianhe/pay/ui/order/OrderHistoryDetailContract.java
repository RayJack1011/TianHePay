package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface OrderHistoryDetailContract {
    interface View extends BaseView {
        void reprintOrderSuccess();

        void reprintOrderFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void reprintOrder(Order order);
    }
}
