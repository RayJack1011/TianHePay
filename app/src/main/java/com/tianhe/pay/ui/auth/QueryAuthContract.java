package com.tianhe.pay.ui.auth;

import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface QueryAuthContract {

    interface View extends BaseView {
        void onQuerySuccess(Auth auth);

        void onQueryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void query(String track);

    }
}
