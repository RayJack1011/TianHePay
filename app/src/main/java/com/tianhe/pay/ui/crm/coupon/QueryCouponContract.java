package com.tianhe.pay.ui.crm.coupon;

import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface QueryCouponContract {

    interface View extends BaseView {
        void onQuerySuccess(Coupon coupon);

        void onQueryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void query(String couponNo);

    }
}
