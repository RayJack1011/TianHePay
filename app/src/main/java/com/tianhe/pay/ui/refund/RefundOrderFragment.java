package com.tianhe.pay.ui.refund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.R;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.RefundDataManager;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.model.RefundablePay;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.util.RecyclerItemClickListener;
import com.tianhe.pay.utils.money.NumberFormats;
import com.tianhe.pay.widget.CustomDialogWy;

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class RefundOrderFragment extends TianHeFragment
        implements RefundOrderContract.View {

    private static final int ID_DIALOG_REFUND_ORDER = BaseDialog.getAutoId();
    private static final int ID_DIALOG_PREPARE = BaseDialog.getAutoId();
    private static final int ID_DIALOG_PRINT = BaseDialog.getAutoId();
    private static final String KEY_IS_SUBMITTING = "submitting";
    private static final String KEY_ORDER_SOURCE = "sourceOrder";

    public static RefundOrderFragment newInstance(Order order) {
        RefundOrderFragment fragment = new RefundOrderFragment();
        fragment.getArguments().putSerializable(KEY_ORDER_SOURCE, order);
        return fragment;
    }

    @Inject
    RefundDataManager refundDataManager;
    @Inject
    RefundOrderContract.Presenter presenter;
    private RecyclerItemClickListener paidItemClickListener;
    private Disposable refundedTotalDisposable;
    private Order orderSource;
    private ViewHolder viewHolder;
    RefundablePayAdapter adapter;
    private boolean isSubmitting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refund_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderSource = (Order) getArguments().getSerializable(KEY_ORDER_SOURCE);
        if (savedInstanceState != null) {
            isSubmitting = savedInstanceState.getBoolean(KEY_IS_SUBMITTING);
        }
        viewHolder = new ViewHolder(view);
        initView();
    }

    private void initView() {
        viewHolder.oriSaleNoTv.setText(orderSource.getOrderHeader().getSaleNo());
        NumberFormat format = NumberFormats.getInstance();

        viewHolder.oriTotalTv.setText(format.format(orderSource.getTotal().getAmount() / 100D));
        viewHolder.oriDiscountTotalTv.setText(format.format(orderSource.getDiscountTotal().getAmount() / 100D));
        viewHolder.oriSaleAmountTv.setText(format.format(orderSource.getAdjustedTotal().getAmount() / 100D));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        viewHolder.paidsRv.setLayoutManager(layoutManager);
        adapter = new RefundablePayAdapter(filterRefundable(), getContext());
        viewHolder.paidsRv.setAdapter(adapter);
        registerPaymentItemClickListener(viewHolder.paidsRv);
        updateRefundedTotal();
        refundedTotalDisposable = refundDataManager.refundedChanged().subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(String refundEvent) {
                updateRefundedTotal();
            }
        });
        viewHolder.oriPaidInfosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        registerForContextMenu(viewHolder.oriPaidInfosView);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        prepareSaleNo();
//    }

    @Override
    public void onResume() {
        super.onResume();
        checkState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SUBMITTING, isSubmitting);
    }

    @Override
    public void onDestroy() {
        unregisterForContextMenu(viewHolder.oriPaidInfosView);
        refundedTotalDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.fragment_refund_order_paid_ori) {
            menu.setHeaderTitle("原支付方式");
            List<PaidInfo> oriPaidInfos = refundDataManager.getOriPaidInfos();
            for (int i = 0; i < oriPaidInfos.size(); i++) {
                PaidInfo paidInfo = oriPaidInfos.get(i);
                menu.add(0, i, i, paidInfo.getPaymentName() + "\t" + paidInfo.getSaleAmount().toString());
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected RefundOrderContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void prepareSaleNoSuccess() {
        dismissDialog(ID_DIALOG_PREPARE);
    }

    private void finish() {
        getActivity().finish();
    }

    @Override
    public void prepareSaleNoFail(String reason) {
        dismissDialog(ID_DIALOG_PREPARE);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.title("初始化退货单失败");
        builder.content("无法获取退货单号, 是否重试");
        builder.canceledOnTouchOutside(false);
        builder.positiveText("重试");
        builder.negativeText("取消退货");
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (which) {
                    case POSITIVE:
                        prepareSaleNo();
                        break;
                    case NEGATIVE:
                        finish();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void refundOrderSuccess(SubmitOrderResult result) {
        dismissDialog(ID_DIALOG_REFUND_ORDER);
        isSubmitting = false;
        setResult();
        showProgress(ID_DIALOG_PRINT, "正在打印退货单...");
        presenter.printRefundOrder();
    }

    @Override
    public void refundOrderFail(String reason) {
        dismissDialog(ID_DIALOG_PRINT);
        isSubmitting = false;
        showMessage("打印失败:" + reason);
    }

    @Override
    public void printRefundOrderSuccess() {
        dismissDialog(ID_DIALOG_PRINT);
        refundDataManager.clear();
        getActivity().finish();
    }

    @Override
    public void printRefundOrderFail(String reason) {
        dismissDialog(ID_DIALOG_PRINT);
        CustomDialogWy.getInstance().showTextDialog(getContext(), reason+",请放入打印纸，重新打印", null, null, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重打
                CustomDialogWy.getInstance().dismiss();
                showProgress(ID_DIALOG_PRINT, "正在打印退货单...");
                presenter.printRefundOrder();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        refundDataManager.clear();
//        if (reason == null) {
//            showMessage("打印失败");
//        } else {
//            showMessage("打印失败:" + reason);
//        }
//        getActivity().finish();
    }
    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra("refunded", true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void prepareSaleNo() {
        showProgress(ID_DIALOG_PREPARE, "初始化退货单...");
        presenter.prepareSaleNo();
    }

    private void updateRefundedTotal() {
//        Log.e("qqq",refundDataManager.getRefundedTotal().toString()+"::::::::::::;jjj");
        if(refundDataManager.getRefundedTotal() != null){
            viewHolder.refundedTotalTv.setText(getString(R.string.refunded_total,
                    refundDataManager.getRefundedTotal().toString()));
        }
    }

    private void registerPaymentItemClickListener(RecyclerView paymentRv) {
        if (paidItemClickListener == null) {
            paidItemClickListener = new RecyclerItemClickListener(paymentRv);
        }
        paidItemClickListener.setItemListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                RefundablePay refundable = (RefundablePay) view.getTag();
                onClickRefundablePay(refundable.getSignpost());
            }
        });
        paymentRv.addOnItemTouchListener(paidItemClickListener);
    }

    private void onClickRefundablePay(PaymentSignpost signpost) {
        nav.enterRefundPay(this, signpost);
    }

    private void checkState() {
        boolean notPreparedSaleNo = (refundDataManager.getSafeSaleNo() == null);
        if (notPreparedSaleNo) {
            prepareSaleNo();
        } else if (refundDataManager.hasRefundPaidComplete() && !isSubmitting) {
            isSubmitting = true;
            showProgress(ID_DIALOG_REFUND_ORDER, "正在提交退货单");
            presenter.refundOrder();
        }
    }

    private List<RefundablePay> filterRefundable() {
        return refundDataManager.filterRefundable();
    }

    private static class ViewHolder {
        TextView oriSaleNoTv;
        TextView oriTotalTv;
        TextView oriDiscountTotalTv;
        View oriPaidInfosView;
        TextView oriSaleAmountTv;
        TextView refundedTotalTv;
        RecyclerView paidsRv;

        public ViewHolder(View view) {
            oriSaleNoTv = view.findViewById(R.id.fragment_refund_order_saleNo_ori);
            oriTotalTv = view.findViewById(R.id.fragment_refund_order_total_ori);
            oriDiscountTotalTv = view.findViewById(R.id.fragment_refund_order_discount_ori);
            oriPaidInfosView = view.findViewById(R.id.fragment_refund_order_paid_ori);
            oriSaleAmountTv = view.findViewById(R.id.fragment_refund_order_saleAmount_ori);
            refundedTotalTv = view.findViewById(R.id.fragment_refund_order_refunded_total);
            paidsRv = view.findViewById(R.id.fragment_refund_order_refundable_list);
        }
    }
}
