package com.tianhe.pay.ui.crm.storedvaluecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.tianhe.pay.BuildConfig;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.di.PerActivity;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;

@PerActivity
public class QueryStoredValueCardFragment extends TianHeFragment
        implements QueryStoredValueCardContract.View {
    private static final int QUERY_DIALOG_ID = BaseDialog.getAutoId();

    @Inject
    CardReader cardReader;
    @Inject
    QueryStoredValueCardContract.Presenter presenter;
    protected ViewHolder viewHolder;
    String track;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_storedvalue_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.passwordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        startSwiper();
    }

    @Override
    public void onDestroy() {
        viewHolder = null;
        super.onDestroy();
    }

    @Override
    protected QueryStoredValueCardContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onQuerySuccess(StoredValueCard card) {
        dismissDialog(QUERY_DIALOG_ID);
        showCardDetail(card);
    }

    private void showCardDetail(final StoredValueCard card) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.title("储值卡详情");
        View cardView = LayoutInflater.from(getContext()).inflate(R.layout.view_storedvalue_card_detail, null);
        TextView cardNoTv = cardView.findViewById(R.id.frag_storedvalue_card_num);
        cardNoTv.setText(card.getCardNo());
        TextView userTv = cardView.findViewById(R.id.frag_storedvalue_card_user);
        userTv.setText(card.getMemberName());
        TextView remainingAmountTv = cardView.findViewById(R.id.frag_storedvalue_card_amount);
        String remainingAmount = card.getRemainingAmount();
        if (Strings.isBlank(remainingAmount)) {
            remainingAmountTv.setText("0.00");
        } else {
            remainingAmountTv.setText(remainingAmount);
        }
        TextView validDataTv = cardView.findViewById(R.id.frag_storedvalue_card_validDate);
        validDataTv.setText(card.getValidDate());
        builder.customView(cardView, false);
        builder.positiveText("确定");
        builder.negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                setResult(card);
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                reset();
            }
        });
        builder.show();
    }

    private void reset() {
        viewHolder.numberTv.setText(null);
        viewHolder.passwordEt.setText(null);
        this.track = null;
    }

    private void setResult(StoredValueCard card) {
        Intent intent = new Intent();
        intent.putExtra("data", card);
        String password = viewHolder.passwordEt.getText().toString();
        intent.putExtra("pwd", password);
        intent.putExtra("track", track);
        getActivity().setResult(Activity.RESULT_OK, intent);
        stopSwiper();
        getActivity().finish();
    }


    @Override
    public void onQueryFail(String reason) {
        dismissDialog(QUERY_DIALOG_ID);
        if (Strings.isBlank(reason)) {
            showMessage("未查询到储值卡信息");
        } else {
            showMessage("未查询到储值卡信息: " + reason);
        }
    }

    private void cancelQuery() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        stopSwiper();
        getActivity().finish();
    }

    private void startSwiper() {
        cardReader.openReadCard(new CardReader.Callback() {
            @Override
            public void openFail(String reason) {
                updateTips("开启读卡设备失败!");
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
        cardReader.closeReadCard();
    }

    private void updateTips(String tips) {
        viewHolder.tipsTv.setText(tips);
    }

    private void setCardNo(CardReader.CardInfo cardInfo) {
        if (cardInfo != null && cardInfo.trackTwo != null) {
            track = cardInfo.trackTwo;
            viewHolder.numberTv.setText(track.split("=")[0]);
        }
    }

    private void query() {
        if (BuildConfig.DEBUG) {
            track = "120000080354=0314712";
            viewHolder.numberTv.setText("120000080354");
        }
        String card = viewHolder.numberTv.getText().toString();
        if (Strings.isBlank(card)) {
            showMessage("卡号不能为空!");
            return;
        }
        String password = viewHolder.passwordEt.getText().toString();
        showProgress(QUERY_DIALOG_ID, "正在查询中, 请稍候...");
        presenter.query(track, password);
    }

    static class ViewHolder {
        public final TextView titleTv;
        public final TextView numberTv;
        public final EditText passwordEt;
        public final TextView tipsTv;
        public final Button cancelButton;
        public final Button okButton;

        public ViewHolder(View view) {
            titleTv = view.findViewById(R.id.fragment_query_card_title);
            numberTv = view.findViewById(R.id.fragment_query_card_number);
            passwordEt = view.findViewById(R.id.fragment_query_card_password);
            tipsTv = view.findViewById(R.id.fragment_query_card_tips);
            cancelButton = view.findViewById(R.id.fragment_query_card_cancel_btn);
            okButton = view.findViewById(R.id.fragment_query_card_ok_btn);
        }
    }
}
