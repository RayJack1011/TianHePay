package com.tianhe.pay.ui.wechatalireprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.adapter.OrderListAdapter;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.utils.Strings;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class WechatAliQueryFragment extends TianHeFragment
        implements WechatAliQueryContract.View {

    private static final int ID_DIALOG_LOADING = BaseDialog.getAutoId();
    private static final int REQUEST_SCAN = 1000;
    public static final String PAY_TYPE = "pay_type";

    private static final int REQUEST_AUTH_FOR_REFUND = 1001;
    private static final int REQUEST_AUTH_FOR_REPRINT = 1002;
    private static final int REQUEST_REFUND_RESULT = 1003;
    private static final int REQUEST_LASTQUARY = 1004;

    @Inject
    Settings settings;
    //    @Inject
//    WechatAliQueryPresenter presenter;
    @Inject
    TencentQueryPresenter tencentQueryPresenter;
    @Inject
    AliQueryPresenter aliQueryPresenter;

    WechatAliQueryContract.Presenter currentPresenter;

    ViewHolder viewHolder;

    public void bindPaymentSignpost(PaymentSignpost signpost) {
        getArguments().putSerializable(PAY_TYPE, signpost);
    }

    private PaymentSignpost getPaymentSignpost() {
        return (PaymentSignpost) getArguments().getSerializable(PAY_TYPE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wechatali_query, container, false);
    }
    OrderListAdapter mAdapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);

        if(CommomData.aliWechatList.size() > 0){//显示支付宝微信单边流水号
            mAdapter = new OrderListAdapter(getContext(),CommomData.aliWechatList);
            viewHolder.orderList.setAdapter(mAdapter);
        }
        viewHolder.orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saleNo = CommomData.aliWechatList.get(position);
                queryByInput(saleNo);
//                requestAuth(REQUEST_AUTH_FOR_REFUND);
            }
        });
        viewHolder.queryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relNo = viewHolder.relNoEt.getText().toString();
                if (Strings.isBlank(relNo)) {
                    showMessage("单号不能为空!");
                    return;
                }
                saleNo = relNo;
                queryByInput(saleNo);
//                requestAuth(REQUEST_AUTH_FOR_REFUND);
            }
        });
        viewHolder.queryLastTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastWechatAliRelNo = settings.getLastWechatAli();
                if (lastWechatAliRelNo == null) {
                    showMessage("没有上笔交易记录, 请输入单号查询!");
                    return;
                }
//                requestAuth(REQUEST_LASTQUARY);
                queryLast();
            }
        });
    }

    private void requestAuth(int requestCode) {
        nav.enterQueryAuthForResult(this, requestCode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.scan) {
            startScan();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    String saleNo;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_AUTH_FOR_REFUND:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasRefundAuth()) {//退货
                        queryByInput(saleNo);
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_AUTH_FOR_REPRINT:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasReprintAuth()) {
//                        reprint();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_LASTQUARY:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasRefundAuth()) {//退货
                        queryLast();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_REFUND_RESULT:
                if (resultCode == RESULT_OK) {
                    boolean refunded = data.getBooleanExtra("refunded", false);
                    if (refunded) {
                        getActivity().finish();
                    }
                }
                break;
            case REQUEST_SCAN:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra("SCAN_RESULT");
                    queryByRelNo(code);
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
//        if (requestCode == REQUEST_SCAN) {
//            if (resultCode == Activity.RESULT_OK) {
//                String code = data.getStringExtra("SCAN_RESULT");
//                queryByRelNo(code);
//            }
//            return;
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected WechatAliQueryContract.Presenter getPresenter() {
        PaymentSignpost signpost = getPaymentSignpost();
        switch (signpost) {
            case ALI:
                currentPresenter = aliQueryPresenter;
                break;
            case WECHAT:
                currentPresenter = tencentQueryPresenter;
                break;
            default:
                throw new IllegalArgumentException("unsupport payment id=" + signpost.getPaymentId());
        }
        return currentPresenter;
    }

    @Override
    public void querySuccess(WechatAliOrder order) {
        dismissDialog(ID_DIALOG_LOADING);
        // 设置重打印标记
        order.setReprint(true);
        nav.enterReprintWechatAli(this, order);
        viewHolder.relNoEt.setText(null);
    }

    @Override
    public void queryFail(String reason) {
        dismissDialog(ID_DIALOG_LOADING);
        showMessage("查询失败:" + reason);
    }

    private void startScan() {
        nav.enterScanBarForResult(this, "请扫描交易单号条码", REQUEST_SCAN);
    }

    private void queryByInput(String relNo) {
//        String relNo = viewHolder.relNoEt.getText().toString();
        queryByRelNo(relNo);
    }

    private void queryLast() {
        String lastWechatAliRelNo = settings.getLastWechatAli();
        queryByRelNo(lastWechatAliRelNo);
    }

    private void queryByRelNo(String relNo) {
        showProgress(ID_DIALOG_LOADING, "正在查询交易记录...");
        currentPresenter.queryWechatAli(relNo);
    }

    private static class ViewHolder {
        EditText relNoEt;
        TextView queryTv;
        TextView queryLastTv;
        LinearLayout orderListLayout;
        ListView orderList;

        public ViewHolder(View view) {
            relNoEt = view.findViewById(R.id.fragment_wechatali_query_input);
            queryTv = view.findViewById(R.id.fragment_wechatali_query_btn);
            queryLastTv = view.findViewById(R.id.fragment_wechatali_query_last_btn);
            orderListLayout = view.findViewById(R.id.order_list_layout);
            orderList = view.findViewById(R.id.order_list);
        }
    }
}
