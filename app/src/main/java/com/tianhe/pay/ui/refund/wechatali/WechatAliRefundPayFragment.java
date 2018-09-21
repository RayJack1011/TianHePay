package com.tianhe.pay.ui.refund.wechatali;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.widget.CustomDialogWy;

import java.util.Map;

import javax.inject.Inject;

public class WechatAliRefundPayFragment extends RefundPayFragment
        implements WechatAliRefundContract.View {

    private static final int ID_DIALOG_REFUNDING = BaseDialog.getAutoId();
    private static final int CODE_REQUEST_SCAN = 1000;
    public static final String KEY_QR_CODE = "result";

//    @Inject
//    WechatAliRefundPresenter presenter;

    @Inject
    TencentRefundPresenter tencentRefundPresenter;
    @Inject
    AliRefundPresenter aliRefundPresenter;

    WechatAliRefundContract.Presenter currentPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder.scanIb.setVisibility(View.GONE);
        viewHolder.scanIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra(KEY_QR_CODE);
                realRefund(code);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refundSuccess(PaidInfo paidInfo) {
        showProgress(ID_DIALOG_REFUNDING, "正在打" + getPaymentName() + "印退款单...");
        currentPresenter.printRefund(paidInfo);
    }

    @Override
    public void refundFailed(String reason) {
        dismissDialog(ID_DIALOG_REFUNDING);
        showMessage(reason);
    }

    @Override
    public void printRefundSuccess() {
        dismissDialog(ID_DIALOG_REFUNDING);
        getActivity().finish();
    }

    @Override
    public void printRefundFail(String reason) {
        dismissDialog(ID_DIALOG_REFUNDING);
        showMessage("退款成功. " + getPaymentName() + "退款单打印失败");
        getActivity().finish();
    }

    @Override
    public void printRefundFail(final String reason, final PaidInfo refund) {
        dismissDialog(ID_DIALOG_REFUNDING);
        CustomDialogWy.getInstance().showTextDialog(getContext(), "退款成功，"+reason+"放入打印纸，重新打印", null, null, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重打
                CustomDialogWy.getInstance().dismiss();
                refundSuccess(refund);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printRefundFail(reason);
            }
        });
    }

    @Override
    protected void refund() {
        realRefund(getRelNo());
    }

    @Override
    protected WechatAliRefundContract.Presenter getPresenter() {
        String paymentId = getPaymentId();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(paymentId);
        switch (signpost) {
            case WECHAT:
                currentPresenter = tencentRefundPresenter;
                break;
            case ALI:
                currentPresenter = aliRefundPresenter;
                break;
            default:
                throw new IllegalArgumentException("unknow payment id=" + paymentId);
        }
        return currentPresenter;
    }

    private void startScan() {
        String scanTips = "请扫描条形码进行" + getPaymentName() + "退款";
        nav.enterScanBarForResult(this, scanTips, CODE_REQUEST_SCAN);
    }

    private void realRefund(String code) {
        showProgress(ID_DIALOG_REFUNDING, "正在退款...");
        currentPresenter.refund(code, getPaymentId());
    }
}
