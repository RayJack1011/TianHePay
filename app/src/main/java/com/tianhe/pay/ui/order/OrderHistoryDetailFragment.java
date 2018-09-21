package com.tianhe.pay.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianhe.pay.R;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.ui.TianHeFragment;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class OrderHistoryDetailFragment extends TianHeFragment
        implements OrderHistoryDetailContract.View {

    private static final int ID_DIALOG_PRINT = BaseDialog.getAutoId();
    private static final int REQUEST_AUTH_FOR_REFUND = 1000;
    private static final int REQUEST_AUTH_FOR_REPRINT = 1001;
    private static final int REQUEST_REFUND_RESULT = 1002;

    private static final String ARGS_ORDER = "order";
    private static final String ARGS_PRINTABLE = "printable";

    public static OrderHistoryDetailFragment newInstance(Order order, boolean printable) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        fragment.getArguments().putSerializable(ARGS_ORDER, order);
        fragment.getArguments().putBoolean(ARGS_PRINTABLE, printable);
        return fragment;
    }

    @Inject
    RefundDataManager refundDataManager;
    @Inject
    OrderHistoryDetailContract.Presenter presenter;
    ViewHolder viewHolder;
    Order order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        initView();
    }

    private void initView() {
        order = (Order) getArguments().getSerializable(ARGS_ORDER);
        if (order == null) {
            return;
        }
        Log.e("qqq","订单详情查询---->"+new Gson().toJson(order));
        viewHolder.saleNoTv.setText(order.getOrderHeader().getSaleNo());
        viewHolder.totalTv.setText(order.getTotal().toString());
        viewHolder.discountTotalTv.setText(order.getDiscountTotal().negate().toString());
        viewHolder.operatorNoTv.setText(order.getOrderHeader().getUserNo());
        viewHolder.terminalNoTv.setText(order.getOrderHeader().getTerminalId());
        viewHolder.pointTv.setText(order.getPointTotal()+"");
        if(TextUtils.isEmpty(order.getOrderHeader().getVipNo())){
            viewHolder.vipNo.setText(" ");
        }else{
            viewHolder.vipNo.setText(order.getOrderHeader().getVipNo()+"");
        }
        if (order.isRefund()) {
            viewHolder.orderTypeTv.setText("退货");
            viewHolder.refundBtn.setVisibility(View.GONE);
        } else if (order.isRefunded()){
            viewHolder.orderTypeTv.setText("销售(已退货)");
            viewHolder.refundBtn.setVisibility(View.GONE);
        } else {
            viewHolder.orderTypeTv.setText("销售");
            viewHolder.refundBtn.setVisibility(View.VISIBLE);
        }
        viewHolder.refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAuth(REQUEST_AUTH_FOR_REFUND);
            }
        });
        viewHolder.timeTv.setText(order.getOrderHeader().getTime());
        initItemView(order.getOrderItems());
        initPaidView(order.getPaidInfos());
    }

    private void initItemView(List<OrderItem> items) {
        for (OrderItem item : items) {
            View itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_cart, viewHolder.productLayout, false);
            TextView nameTv = itemView.findViewById(R.id.view_cart_item_name);
            TextView goodsNoTv = itemView.findViewById(R.id.view_cart_item_number);
            TextView discountTv = itemView.findViewById(R.id.view_cart_item_discount);
            TextView priceTv = itemView.findViewById(R.id.view_cart_item_price);
            TextView quantityTv = itemView.findViewById(R.id.view_cart_item_quantity);
            TextView shishou = itemView.findViewById(R.id.view_cart_item_price_momney);
            nameTv.setText(item.getName());
            goodsNoTv.setText(item.getBarcode());
            priceTv.setText(getString(R.string.item_cart_price, item.getOldPrice().toString()));
            discountTv.setText(getString(R.string.item_cart_discount, item.getDiscountTotal().negate().toString()));
            quantityTv.setText(getString(R.string.item_cart_quantity, item.getQuantity()));
            shishou.setText("实收:"+item.getSaleAmount().getYuan());
            viewHolder.productLayout.addView(itemView);
        }
    }

    private void initPaidView(List<PaidInfo> paidInfos) {
        for (PaidInfo paidInfo : paidInfos) {
            View paidView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_paid_info, viewHolder.productLayout, false);
            TextView nameTv = paidView.findViewById(R.id.item_paid_name);
            TextView amountTv = paidView.findViewById(R.id.item_paid_amount);
            TextView relTv = paidView.findViewById(R.id.item_paid_relNo);
            nameTv.setText(paidInfo.getPaymentName());
            amountTv.setText(paidInfo.getSaleAmount().toString());
            relTv.setText(paidInfo.getBillNo());
            viewHolder.paidLayout.addView(paidView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_AUTH_FOR_REFUND:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasRefundAuth()) {
                        entryRefundOrder();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_AUTH_FOR_REPRINT:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasReprintAuth()) {
                        reprint();
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
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isPrintable()) {
            inflater.inflate(R.menu.order_reprint, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean isPrintable() {
        return getArguments().getBoolean(ARGS_PRINTABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.reprint:
                requestAuth(REQUEST_AUTH_FOR_REPRINT);
//                reprint();
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reprint() {
        showProgress(ID_DIALOG_PRINT, "正在重打印...");
        presenter.reprintOrder(order);
    }

    @Override
    protected OrderHistoryDetailContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void reprintOrderSuccess() {
        dismissDialog(ID_DIALOG_PRINT);
        getActivity().finish();
    }

    @Override
    public void reprintOrderFail(String reason) {
        dismissDialog(ID_DIALOG_PRINT);
        showMessage("重打印失败:" + reason);
    }

    private void entryRefundOrder() {
        refundDataManager.prepareForRefund(order);
        nav.enterRefundOrder(this, order, REQUEST_REFUND_RESULT);
    }

    private void requestAuth(int requestCode) {
        nav.enterQueryAuthForResult(this, requestCode);
    }

    private static class ViewHolder {
        TextView saleNoTv;          // 单号
        TextView totalTv;           // 订单原价
        TextView discountTotalTv;   // 折扣总额
        TextView operatorNoTv;      // 操作人编号
        TextView terminalNoTv;      // 终端编号
        TextView pointTv;           // 积分
        TextView vipNo;           // 会员卡号
        TextView orderTypeTv;       // 交易类型: 销售/退货
        TextView timeTv;
        LinearLayout productLayout; // 商品区域
        LinearLayout paidLayout;    // 支付区域
        Button refundBtn;           // 退货按钮

        public ViewHolder(View view) {
            saleNoTv = view.findViewById(R.id.order_detail_saleNo);
            totalTv = view.findViewById(R.id.order_detail_total);
            discountTotalTv = view.findViewById(R.id.order_detail_discount);
            operatorNoTv = view.findViewById(R.id.order_detail_casher);
            terminalNoTv = view.findViewById(R.id.order_detail_posId);
            pointTv = view.findViewById(R.id.order_detail_points);
            vipNo = view.findViewById(R.id.order_detail_vipNo);
            orderTypeTv = view.findViewById(R.id.order_detail_saleType);
            timeTv = view.findViewById(R.id.order_detail_time);
            orderTypeTv = view.findViewById(R.id.order_detail_saleType);
            productLayout = view.findViewById(R.id.fragment_refund_detail_itemList_container);
            paidLayout = view.findViewById(R.id.fragment_refund_detail_paidList_container);
            refundBtn = view.findViewById(R.id.fragment_refund_detail_ok_btn);
        }
    }
}
