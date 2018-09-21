package com.tianhe.pay.ui.crm.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.tianhe.pay.R;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;

public class QueryCouponFragment extends TianHeFragment
        implements QueryCouponContract.View {

    private static final int QUERY_DIALOG_ID = BaseDialog.getAutoId();
    @Inject
    QueryCouponContract.Presenter presenter;

    private ViewHolder viewHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_coupon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
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
    protected QueryCouponContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onQuerySuccess(Coupon coupon) {
        dismissDialog(QUERY_DIALOG_ID);
        showCouponDetail(coupon);
    }

    private void showCouponDetail(final Coupon coupon) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        String couponName = coupon.getCouponName();
        if (Strings.isBlank(couponName)) {
            builder.title("券详情");
        } else {
            builder.title(couponName);
        }
        View couponView = LayoutInflater.from(getContext()).inflate(R.layout.view_coupon_detail, null);
        TextView cardNoTv = couponView.findViewById(R.id.frag_coupon_num);
        cardNoTv.setText(coupon.getCouponNo());
        TextView amountTv = couponView.findViewById(R.id.frag_coupon_amount);
        amountTv.setText(coupon.getAmount());
        TextView validDateTv = couponView.findViewById(R.id.frag_coupon_validDate);
        String startDate = coupon.getStartDate();
        String endDate = coupon.getEndDate();
        if (Strings.isBlank(startDate)) {
            validDateTv.setText(endDate);
        } else {
            validDateTv.setText(startDate +" - " + endDate);
        }
        builder.customView(couponView, false);
        builder.positiveText("确定");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                setResult(coupon);
            }
        });
        builder.show();
    }

    private void setResult(Coupon coupon) {
        Intent intent = new Intent();
        intent.putExtra("data", coupon);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    public void onQueryFail(String reason) {
        dismissDialog(QUERY_DIALOG_ID);
        showMessage(reason);
    }

    private void query() {
        String cardNo = viewHolder.numberEt.getText().toString();
        if (Strings.isBlank(cardNo)) {
            showMessage("券号不能为空!");
            return;
        }
        showProgress(QUERY_DIALOG_ID, "正在查询中, 请稍候...");
        presenter.query(cardNo);
    }

    private void cancelQuery() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    static class ViewHolder {
        public final TextView titleTv;
        public final EditText numberEt;
        public final Button cancelButton;
        public final Button okButton;

        public ViewHolder(View view) {
            titleTv = view.findViewById(R.id.fragment_query_card_title);
            numberEt = view.findViewById(R.id.fragment_query_card_number);
            cancelButton = view.findViewById(R.id.fragment_query_card_cancel_btn);
            okButton = view.findViewById(R.id.fragment_query_card_ok_btn);
        }
    }
}
