package com.tianhe.pay.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;
import javax.inject.Named;

public class QueryAuthFragment extends TianHeFragment
        implements QueryAuthContract.View {

    private static final int ID_DIALOG_QUERY = BaseDialog.getAutoId();
    private static final String SPLIT = ":";

    @Inject
    Global global;
    @Inject
    QueryAuthContract.Presenter presenter;
    @Inject
    @Named("md5Sign")
    Sign md5Sign;
    ViewHolder viewHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_auth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelQuery();
            }
        });
        viewHolder.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAuth();
            }
        });
    }

    private void cancelQuery() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    protected QueryAuthContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onQuerySuccess(Auth auth) {
        dismissDialog(ID_DIALOG_QUERY);
        Intent intent = new Intent();
        intent.putExtra("data", auth);
        getActivity().setResult(Activity.RESULT_OK, intent);
        CommomData.authUser = viewHolder.userNoEt.getText().toString();
        getActivity().finish();
    }

    @Override
    public void onQueryFail(String reason) {
        dismissDialog(ID_DIALOG_QUERY);
        showMessage("无法查询到权限:" + reason);
    }

    private void queryAuth() {
        String userNo = viewHolder.userNoEt.getText().toString();
        if (Strings.isBlank(userNo)) {
            showMessage("账号不能未空!");
            return;
        }
        String pwd = viewHolder.pwdEt.getText().toString();
        if (Strings.isBlank(pwd)) {
            showMessage("密码不能未空!");
            return;
        }
        showProgress(ID_DIALOG_QUERY, "正在查询权限");
        String authString = global.getShopNo() + SPLIT + userNo + SPLIT + md5Sign.sign(userNo + pwd);
        presenter.query(authString);
    }

    static class ViewHolder {
        public final TextView titleTv;
        public final EditText userNoEt;
        public final EditText pwdEt;
        public final Button cancelButton;
        public final Button okButton;

        public ViewHolder(View view) {
            titleTv = view.findViewById(R.id.fragment_query_auth_title);
            userNoEt = view.findViewById(R.id.fragment_query_auth_user_no);
            pwdEt = view.findViewById(R.id.fragment_query_auth_password);
            cancelButton = view.findViewById(R.id.fragment_query_card_cancel_btn);
            okButton = view.findViewById(R.id.fragment_query_card_ok_btn);
        }
    }
}
