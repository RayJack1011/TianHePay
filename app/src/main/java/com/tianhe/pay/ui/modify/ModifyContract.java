package com.tianhe.pay.ui.modify;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

/**
 * Created by wangya3 on 2018/3/27.
 */

public interface ModifyContract {
    interface View extends BaseView {
        CharSequence username();
        CharSequence oldPassword();
        CharSequence newPassword();


        void modifySuccess();

        void modifyFailed(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void modify();
    }
}
