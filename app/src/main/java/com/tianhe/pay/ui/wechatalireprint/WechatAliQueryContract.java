package com.tianhe.pay.ui.wechatalireprint;

import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.data.wechatali.query.TongguanQueryResponse;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface WechatAliQueryContract {
    interface View extends BaseView {
        void querySuccess(WechatAliOrder response);

        void queryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {
        void queryWechatAli(String relNo);
    }
}
