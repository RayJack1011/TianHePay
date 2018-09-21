package com.tianhe.pay.ui.payment.wechatali;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.payment.Payment;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.money.Money;
import com.tianhe.pay.widget.CustomDialogWy;

import javax.inject.Inject;

public class WechatAliPayFragment extends PayFragment implements WechatAliPayContract.View {
    private static final int ID_DIALOG_PAY = BaseDialog.getAutoId();
    private static final int CODE_REQUEST_SCAN = 1000;
    public static final String KEY_QR_CODE = "result";

    //    @Inject
//    WechatAliPayPresenter payPresenter;
    @Inject
    TencentPayPresenter tencentPayPresenter;
    @Inject
    AliPayPresenter aliPayPresenter;

    WechatAliPayContract.Presenter currentPresenter;
    MdDialog.Builder builder;

    MdDialog.Builder builder1;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra(KEY_QR_CODE);
                realPay(code);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected WechatAliPayContract.Presenter getPresenter() {
        Payment payment = getPayment();
        PaymentSignpost signpost = PaymentSignpost.fromPaymentId(payment.getPaymentId());
        switch (signpost) {
            case ALI:
                currentPresenter = aliPayPresenter;
                break;
            case WECHAT:
                currentPresenter = tencentPayPresenter;
                break;
            default:
                    throw new IllegalArgumentException("unknow payment is id=" + payment.getPaymentId());
        }
        return currentPresenter;
    }

    @Override
    protected void pay() {
        startScan();
    }

    @Override
    public void paySuccess(PaidInfo paidInfo) {
        showProgress(ID_DIALOG_PAY, "正在打印签购单...");
//        if(paidInfo.getPaymentId().equals("03")||paidInfo.getPaymentId().equals("02")){
//            if(CommomData.aliWechatList.size() < 20){
//                CommomData.aliWechatList.add(CommomData.shopNos+cartManager.getSafeSaleNo());//支付宝微信单边
//            }else{
//                CommomData.aliWechatList.remove(0);
//                CommomData.aliWechatList.add(CommomData.shopNos+cartManager.getSafeSaleNo());//支付宝微信单边
//            }
//        }
        currentPresenter.print(paidInfo);
    }

    @Override
    public void payFailed(String reason) {
        dismissDialog(ID_DIALOG_PAY);
        showMessage(reason);
    }

    @Override
    public void printSuccess() {
        dismissDialog(ID_DIALOG_PAY);
        getActivity().finish();
    }

    @Override
    public void printFail(final String reason) {
        dismissDialog(ID_DIALOG_PAY);
        if (reason == null) {
            showMessage("打印微信签购单失败");
        } else {
            showMessage("打印微信签购单失败" + reason);
        }
        getActivity().finish();

    }

    @Override
    public void printFail(final String reason, final PaidInfo paidInfo) {
        dismissDialog(ID_DIALOG_PAY);
        CustomDialogWy.getInstance().showTextDialog(getContext(), reason+",请放入打印纸，重新打印", null, null, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重打
                CustomDialogWy.getInstance().dismiss();
                paySuccess(paidInfo);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printFail(reason);
            }
        });

    }

    int payId;
    @Override
    public void waitingCustomerPay() {
        builder = new MdDialog.Builder(ID_DIALOG_PAY)
                .message("等待客户付款...")
                .negativeText("取消")
                .progress();
        showDialog(builder);
        payId = ID_DIALOG_PAY;
    }

    @Override
    public void waitingQuary() {
        builder = new MdDialog.Builder(ID_DIALOG_PAY)
                .message("查询中（是否付款）..")
                .negativeText("取消")
                .progress();
        showDialog(builder);
        payId = ID_DIALOG_PAY;
    }


    MdDialog.Builder builders = new MdDialog.Builder(ID_DIALOG_PAY);
    @Override
    public void waitingForPay(String msg) {
        dismissDialog(payId);
        builders.message(msg);
        builders.progress();
        showDialog(builders);
//        showProgress(ID_DIALOG_PAY, msg);
    }

    @Override
    public void onDialogCancel(int dialogId) {
        if (dialogId == ID_DIALOG_PAY) {
            currentPresenter.cancelWaiting();
            dismissDialog(ID_DIALOG_PAY);
        }
    }

    private void startScan() {
        String scanTips = "请扫描二维码进行" + getPaymentName();
        nav.enterScanBarForResult(this, scanTips, CODE_REQUEST_SCAN);
    }

    private void realPay(String code) {
        showProgress(ID_DIALOG_PAY, "支付交易中...");
        currentPresenter.pay(getPayment(), new Money(willPayCent), code);
    }

    @Override
    public void showMessage(CharSequence message) {
        super.showMessage(message);
        Toast.makeText(getContext(),message.toString(),Toast.LENGTH_LONG).show();
    }
}
