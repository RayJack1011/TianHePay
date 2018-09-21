package com.tianhe.pay.ui.login;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface LoginContract {
    interface View extends BaseView {
        CharSequence username();
        CharSequence password();

        void loginSuccess();

        void loginFailed(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void login();
    }
}
