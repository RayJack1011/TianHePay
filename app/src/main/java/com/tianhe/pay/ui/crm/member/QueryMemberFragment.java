package com.tianhe.pay.ui.crm.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.devices.CardReader;
import com.tianhe.pay.R;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;

public class QueryMemberFragment extends TianHeFragment implements QueryMemberContract.View {

    private static final int ID_DIALOG_QUERY = BaseDialog.getAutoId();
    private static final int ID_DIALOG_DETAIL = BaseDialog.getAutoId();
    @Inject
    CardReader cardReader;
    @Inject
    QueryMemberContract.Presenter presenter;

    private ViewHolder viewHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_member, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        startSwiper(); // 开启读卡设备
    }

    private void initView(View view) {
        viewHolder = new ViewHolder(view);
        viewHolder.numberEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    query();
                    return true;
                }
                return false;
            }
        });
        viewHolder.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
        viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelQuery();
            }
        });
    }

    @Override
    public void onDestroy() {
        viewHolder = null;
        super.onDestroy();
    }

    @Override
    protected QueryMemberContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onQuerySuccess(Member member) {
        dismissDialog(ID_DIALOG_QUERY);
        showMemberDetail(member);
    }

    private void showMemberDetail(final Member member) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.title("会员详情");
        View memberView = LayoutInflater.from(getContext()).inflate(R.layout.view_member_detail, null);
        TextView cardNoTv = memberView.findViewById(R.id.frag_vipdetail_num);
        cardNoTv.setText(member.getCard().cardNo);
        TextView userTv = memberView.findViewById(R.id.frag_vipdetail_user);
        userTv.setText(member.getMemeberName());
        TextView phoneTv = memberView.findViewById(R.id.frag_vipdetail_phone);
        String phone = member.getMemeberMoblieNo();
        if (Strings.isBlank(phone)) {
            phoneTv.setVisibility(View.GONE);
        } else {
            phoneTv.setText(phone);
        }
        TextView pointsTv = memberView.findViewById(R.id.frag_vipdetail_points);
        pointsTv.setText(member.getCard().remainingPoint);
        builder.customView(memberView, false);
        builder.positiveText("确定");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                setResult(member);
            }
        });
        builder.show();
    }

    private void setResult(Member member) {
        Intent intent = new Intent();
        intent.putExtra("data", member);
        getActivity().setResult(Activity.RESULT_OK, intent);
        stopSwiper();
        getActivity().finish();
    }

    @Override
    public void onQueryFail(String reason) {
        dismissDialog(ID_DIALOG_QUERY);
        if (reason == null) {
            showMessage("未查询到会员信息!");
        } else {
            showMessage(reason);
        }
    }

    private void startSwiper() {
        cardReader.openReadCard(new CardReader.Callback() {
            @Override
            public void openFail(String reason) {
                updateTips("开启设备失败:" + reason);
            }

            @Override
            public void onPrepared() {
                updateTips("刷卡设备已就绪.");
            }

            @Override
            public void onRead(CardReader.CardInfo cardInfo) {
                setCardNo(cardInfo);
            }
        });
    }

    private void stopSwiper() {
        Log.e("QueryMember", "stopSwiper");
        cardReader.closeReadCard();
    }

    private void updateTips(String tips) {
        viewHolder.tipsTv.setText(tips);
    }

    private void setCardNo(CardReader.CardInfo cardInfo) {
//        cardInfo.trackTwo = "；680000207760=0400473？";
        if (cardInfo != null && cardInfo.trackTwo != null) {
            String cardNo = cardInfo.trackTwo.split("=")[0];
//            viewHolder.numberEt.setText("680000207760");
            viewHolder.numberEt.setText(cardNo);
            viewHolder.numberEt.setSelection(cardNo.length());
        }
    }

    private void query() {
        String cardNo = viewHolder.numberEt.getText().toString();
        if (Strings.isBlank(cardNo)) {
            showMessage("卡号不能为空!");
            return;
        }
        showProgress(ID_DIALOG_QUERY, "正在查询中, 请稍候...");
        presenter.query(cardNo);
    }

    private void cancelQuery() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        stopSwiper();
        getActivity().finish();
    }

    static class ViewHolder {
        public final TextView titleTv;
        public final EditText numberEt;
        public final TextView tipsTv;
        public final Button cancelButton;
        public final Button okButton;

        public ViewHolder(View view) {
            titleTv = view.findViewById(R.id.fragment_query_card_title);
            numberEt = view.findViewById(R.id.fragment_query_card_number);
            tipsTv = view.findViewById(R.id.fragment_query_card_tips);
            cancelButton = view.findViewById(R.id.fragment_query_card_cancel_btn);
            okButton = view.findViewById(R.id.fragment_query_card_ok_btn);
        }
    }
}
