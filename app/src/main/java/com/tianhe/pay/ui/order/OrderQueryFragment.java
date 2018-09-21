package com.tianhe.pay.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;

public class OrderQueryFragment extends TianHeFragment
    implements OrderQueryContract.View {

    private static final int ID_DIALOG_QUERY = BaseDialog.getAutoId();

    @Inject
    OrderQueryContract.Presenter presenter;
    ViewHolder viewHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_refundable_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.queryOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
    }

    @Override
    protected OrderQueryContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void querySuccess(Order order) {
        dismissDialog(ID_DIALOG_QUERY);
        nav.enterOrderHistoryDetail(this, order, false);
        getActivity().finish();
    }

    @Override
    public void queryHistoryFail(String reason) {
        dismissDialog(ID_DIALOG_QUERY);
        if (reason == null) {
            showMessage("查询失败: " + reason);
        } else {
            showMessage(reason);
        }
    }

    private void query() {
        String saleNo = viewHolder.saleNoEt.getText().toString();
        if (Strings.isBlank(saleNo)) {
            showMessage("请输入交易单号!");
            return;
        }
        showProgress(ID_DIALOG_QUERY, "查询中...");
        presenter.queryOrderBySaleNo(saleNo);
    }

    private static class ViewHolder {
        EditText saleNoEt;
        TextView queryOk;

        public ViewHolder(View view) {
            saleNoEt = view.findViewById(R.id.fragment_query_refundable_order_input);
            queryOk = view.findViewById(R.id.fragment_query_refundable_order_query_btn);
        }
    }
}
