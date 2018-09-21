package com.tianhe.pay.ui.welcome;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.data.app.AppUpgrade;

import java.io.File;

public interface WelcomeContract {
    interface View extends BaseView {
        void lastAppUpgrade(boolean needUpdate, AppUpgrade upgrade);

        void downloadingProgress(int progress);

        void downloadFail(String reason);

        void downloadSuccess(File file);
    }

    interface Presenter extends SavablePresenter<View> {
        void checkAppUpdate();

        void downloadLastVersion();
    }
}
