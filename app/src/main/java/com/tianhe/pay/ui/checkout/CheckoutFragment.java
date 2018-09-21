package com.tianhe.pay.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.crm.gift.ProcessGift;
import com.tianhe.pay.data.crm.gift.ProcessGiftResult;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.di.PerActivity;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.util.RecyclerItemClickListener;
import com.tianhe.pay.utils.money.Money;
import com.tianhe.pay.widget.CustomDialogWy;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;
import static com.tianhe.pay.model.CartManager.Event.ORDER_PAID_CHANGED;

@PerActivity
public class CheckoutFragment extends TianHeFragment implements CheckoutContract.View {

    private static final int ID_DIALOG_CALCULATING = BaseDialog.getAutoId();
    private static final int ID_DIALOG_CALCULATE_ERROR = BaseDialog.getAutoId();
    private static final int ID_DIALOG_SUBMITTING = BaseDialog.getAutoId();
    private static final int ID_DIALOG_SUBMIT_ERROR = BaseDialog.getAutoId();
    private static final int ID_DIALOG_PRINTING_ORDER = BaseDialog.getAutoId();
    private static final int ID_DIALOG_PROCESS_GIFT = BaseDialog.getAutoId();
    private static final String KEY_IS_SUBMITTING = "submitting";

    private static final int REQUEST_AUTH_FOR_REFUND = 1000;
    private static final int REQUEST_AUTH_FOR_REPRINT = 1001;
    private static final int REQUEST_REFUND_RESULT = 1002;

    @Inject
    CheckoutContract.Presenter presenter;
    @Inject
    CartManager cartManager;

    private ViewHolder viewHolder;
    private RecyclerItemClickListener paymentItemClickListener;
    private Disposable paidTotalDisposable;
    private boolean isSubmitting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        if (savedInstanceState != null) {
            isSubmitting = savedInstanceState.getBoolean(KEY_IS_SUBMITTING);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPendingAmount();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!cartManager.isCalculatedOrder()) {
            calculateOrder();
        } else {
            calculateSuccess();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SUBMITTING, isSubmitting);
    }

    @Override
    public void onDestroy() {
        if (paidTotalDisposable != null) {
            paidTotalDisposable.dispose();
        }
        super.onDestroy();
    }

    private void calculateOrder() {
        showProgress(ID_DIALOG_CALCULATING, R.string.checkout_calculating_message);
        presenter.calculateOrder();
    }

    @Override
    protected CheckoutContract.Presenter getPresenter() {
        return presenter;
    }

    String prepayMoneys = "";

    @Override
    public void calculateSuccess() {
        dismissDialog(ID_DIALOG_CALCULATING);

        Money subtotal = cartManager.getSubTotal();
        Money dueTotal = cartManager.getAdjustedTotal();

        // 折扣用负数显示
        Money discountTotal = dueTotal.subtract(subtotal);
        viewHolder.subtotalTv.setText(subtotal.toString());
        viewHolder.dueTotalTv.setText(dueTotal.toString());
        viewHolder.discountTotalTv.setText(discountTotal.toString());
        updatePaidTotal();
        paidTotalDisposable = cartManager.cartChanged().subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(String cartEvent) {
                if (ORDER_PAID_CHANGED.equals(cartEvent)) {
                    updatePaidTotal();
                }
            }
        });
        presenter.loadUsablePayment();
    }

    @Override
    public void calculateFail(String reason) {
        dismissDialog(ID_DIALOG_CALCULATING);
        MdDialog.Builder builder = new MdDialog.Builder(ID_DIALOG_CALCULATE_ERROR);
        builder.title(R.string.error_checkout_calculate_fail_title)
                .message(R.string.error_checkout_calculate_fail_content)
                .positiveText(R.string.checkout_try_again_calculate)
                .negativeText(R.string.checkout_cancel_calculate);
        showDialog(builder);
    }

    @Override
    public void renderUsablePayments(List<Payment> payments) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        viewHolder.paymentRv.setLayoutManager(layoutManager);
        viewHolder.paymentRv.setAdapter(new UsablePaymentAdapter(payments));
        registerPaymentItemClickListener(viewHolder.paymentRv);
    }

    @Override
    public void loadUsablePaymentsFail(String reason) {
        showMessage(reason);
    }

    @Override
    public void submitOrderSuccess(SubmitOrderResult result) {
        dismissDialog(ID_DIALOG_SUBMITTING);
        isSubmitting = false;
        printBill();
    }

    @Override
    public void submitOrderFail(String reason) {
        dismissDialog(ID_DIALOG_SUBMITTING);
        isSubmitting = false;

        MdDialog.Builder builder = new MdDialog.Builder(ID_DIALOG_SUBMIT_ERROR);
        builder.title("提交订单失败")
//                .message(reason)
                .positiveText("重试")
                .negativeText("取消订单");
        showDialog(builder);
    }

    @Override
    public void printOrderSuccess() {
        dismissDialog(ID_DIALOG_PRINTING_ORDER);
        // 需要处理赠券
//        cartManager.hasCouponList();
        if (cartManager.hasCouponGift()) {
//            showProgress(ID_DIALOG_PROCESS_GIFT, "正在处理赠券...");
            presenter.processGift();
        }
//        else if(cartManager.hasCouponList()){
////            showProgress(ID_DIALOG_PROCESS_GIFT, "正在处理quanhao...");
//            presenter.processGift();
//        }
        else {
            cartManager.clear();
            getActivity().finish();
        }
    }

    @Override
    public void printOrderFail(final String reason) {
        dismissDialog(ID_DIALOG_PRINTING_ORDER);
        CustomDialogWy.getInstance().showTextDialog(getContext(), reason+",请放入打印纸，重新打印", null, null, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重打
                CustomDialogWy.getInstance().dismiss();
                printBill();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                printOrderFail(reason);
            }
        });
//        if (cartManager.hasCouponGift()) {
//            showProgress(ID_DIALOG_PROCESS_GIFT, "正在处理赠券...");
//            presenter.processGift();
//        } else {
//            cartManager.clear();
//            getActivity().finish();
//            if (reason == null) {
//                showMessage("打印失败");
//            } else {
//                showMessage("打印失败: " + reason);
//            }
//        }
    }

    @Override
    public void printOrderFail(final String reason, SubmitOrderResult result) {
        dismissDialog(ID_DIALOG_PRINTING_ORDER);
        CustomDialogWy.getInstance().showTextDialog(getContext(), reason+",请放入打印纸，重新打印", null, null, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重打
                CustomDialogWy.getInstance().dismiss();
                printBill();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printOrderFail(reason);
            }
        });
    }

    @Override
    public void processGiftSuccess(ProcessGiftResult gift) {
        showProgress(ID_DIALOG_PROCESS_GIFT, "正在打印纸质券...");
        presenter.printGiftCoupon(gift.getGiftCoupons());
    }

    @Override
    public void processGiftFail(String message) {
        dismissDialog(ID_DIALOG_PROCESS_GIFT);
        cartManager.clear();
        getActivity().finish();
        if (message == null) {
            showMessage("赠券失败, 请联系管理人员!");
        } else {
            Log.e("qqq", message + "::::::::::;;cuowuxinxi");
            showMessage("赠券失败: 券已送完");
        }
    }

    @Override
    public void processGiftFail(String message, ProcessGiftResult gift) {

    }

    @Override
    public void printGiftCouponFail(String message) {
        dismissDialog(ID_DIALOG_PROCESS_GIFT);
        cartManager.clear();
        getActivity().finish();
        if (message == null) {
            showMessage("打印赠券失败");
        } else {
            showMessage("打印赠券失败: " + message);
        }
    }

    @Override
    public void printGiftCouponSuccess() {
        dismissDialog(ID_DIALOG_PROCESS_GIFT);
        cartManager.clear();
        getActivity().finish();
    }

    @Override
    public void onDialogOk(int dialogId) {
        if (dialogId == ID_DIALOG_CALCULATE_ERROR) {
            calculateOrder();
            dismissDialog(dialogId);
            return;
        }
        if (dialogId == ID_DIALOG_SUBMIT_ERROR) {
            dismissDialog(dialogId);
            submitBill();
            return;
        }
    }

    @Override
    public void onDialogCancel(int dialogId) {
        if (dialogId == ID_DIALOG_CALCULATE_ERROR
                || dialogId == ID_DIALOG_SUBMIT_ERROR) {
            cartManager.clear();
            dismissDialog(dialogId);
            getActivity().finish();
            return;
        }
    }

    Payment payments;

    private void registerPaymentItemClickListener(RecyclerView paymentRv) {
        if (paymentItemClickListener == null) {
            paymentItemClickListener = new RecyclerItemClickListener(paymentRv);
        }
        paymentItemClickListener.setItemListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Payment payment = (Payment) view.getTag();
                onPaymentClick(payment);
            }
        });
        paymentRv.addOnItemTouchListener(paymentItemClickListener);
    }

    private void onPaymentClick(Payment payment) {
        payments = payment;
        if (PaymentSignpost.ALI.getPaymentId().equals(payment.getPaymentId())
                ) {
            if (cartManager.hasAliPaid()) {
                showMessage("本单支付宝只能支付一次");
                return;
            }
            nav.enterPay(this, payment);
        } else if (PaymentSignpost.WECHAT.getPaymentId().equals(payment.getPaymentId())) {
            if (cartManager.hasWechatPaid()) {
                showMessage("本单微信只能支付一次");
                return;
            }
            nav.enterPay(this, payment);
        } else if (PaymentSignpost.ALI_POS.getPaymentId().equals(payment.getPaymentId()) ||
                PaymentSignpost.WECHAT_POS.getPaymentId().equals(payment.getPaymentId()) ||
                PaymentSignpost.BANKCARD_POS.getPaymentId().equals(payment.getPaymentId())) {//设置权限卡
            requestAuth(REQUEST_AUTH_FOR_REFUND);
        } else {
            nav.enterPay(this, payment);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_AUTH_FOR_REFUND:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasRefundAuth()) {//退货
                        nav.enterPay(this, payments);
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

    private void requestAuth(int requestCode) {
        nav.enterQueryAuthForResult(this, requestCode);
    }

    private void updateViewByCart(String event) {
        if (ORDER_PAID_CHANGED.equals(event)) {
            updatePaidTotal();
        }
    }

    private void updatePaidTotal() {
        viewHolder.paidTotalTv.setText(cartManager.getPaidTotal().toString());
        double d = Double.parseDouble(cartManager.getAdjustedTotal().toString());//总价
        double d1 = Double.parseDouble(cartManager.getPaidTotal().toString());//已付
        double d2 = d - d1;//未付
        DecimalFormat df = new DecimalFormat("######0.00");
        viewHolder.prePayMoney.setText(df.format(d2));
    }

    /**
     * 检测剩余支付金额,如果已完成整单支付,提交
     */
    private void checkPendingAmount() {
        if (cartManager.isCalculatedOrder() && !cartManager.hasRemainPendingAmount()) {
            submitBill();
        }
    }

    private void submitBill() {
        if (!isSubmitting) {
            isSubmitting = true;
            showProgress(ID_DIALOG_SUBMITTING, "正在提交订单...");
            presenter.submitOrder();
        }
    }

    private void printBill() {
//        1111111111111
        showProgress(ID_DIALOG_PRINTING_ORDER, "正在打印账单...");
        presenter.printOrder();
    }

    private static class ViewHolder {
        TextView subtotalTv;        // 订单原价
        TextView discountTotalTv;   // 折扣金额
        TextView dueTotalTv;        // 应付
        TextView paidTotalTv;       // 已支付
        TextView prePayMoney;       // 未支付
        RecyclerView paymentRv;     // 可选用的支付方式


        public ViewHolder(View view) {
            subtotalTv = view.findViewById(R.id.view_order_total_subtotal);
            discountTotalTv = view.findViewById(R.id.view_order_total_discount);
            dueTotalTv = view.findViewById(R.id.view_order_total_due);
            paidTotalTv = view.findViewById(R.id.fragment_checkout_paid_total);
            paymentRv = view.findViewById(R.id.fragment_checkout_payment_list);
            prePayMoney = view.findViewById(R.id.pre_pay);
        }
    }

}
