package com.tianhe.pay.ui.crm.member;

import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;

public interface QueryMemberContract {

    interface View extends BaseView {
        void onQuerySuccess(Member member);

        void onQueryFail(String reason);
    }

    interface Presenter extends SavablePresenter<View> {

        void query(String cardNo);

    }
}
