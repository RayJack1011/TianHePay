package com.tianhe.pay.ui.crm.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianhe.pay.R;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.TianHeFragment;

public class MemberDetailFragment extends TianHeFragment {

    private Member member;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.view_member_detail, container, false);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

}
