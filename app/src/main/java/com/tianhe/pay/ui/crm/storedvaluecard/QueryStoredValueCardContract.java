package com.tianhe.pay.ui.crm.storedvaluecard;

import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface QueryStoredValueCardContract {
    interface View extends BaseView {
        void onQuerySuccess(StoredValueCard card);

        void onQueryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void query(String track, String password);

    }
}
