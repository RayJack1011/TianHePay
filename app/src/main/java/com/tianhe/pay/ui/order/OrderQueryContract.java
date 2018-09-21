package com.tianhe.pay.ui.order;

import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface OrderQueryContract {
    interface View extends BaseView {
        void querySuccess(Order order);

        void queryHistoryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void queryOrderBySaleNo(String saleNo);
    }
}
