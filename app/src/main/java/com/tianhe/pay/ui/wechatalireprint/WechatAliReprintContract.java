package com.tianhe.pay.ui.wechatalireprint;

import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface WechatAliReprintContract {

    interface View extends BaseView {
        void reprintSuccess();

        void reprintFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void reprint(WechatAliOrder order);
    }
}
