package com.tianhe.pay.ui.refund.bankcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tianhe.pay.data.bankcard.DataUtils;
import com.tianhe.pay.data.bankcard.refund.BankcardRefundRequest;
import com.tianhe.pay.data.bankcard.refund.BankcardRefundResponse;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;

import javax.inject.Inject;

public class BankcardRefundPayFragment extends RefundPayFragment {

    private static final int REQUEST_BANKCARD_MIS_REFUND = 1000;

    @Inject
    Global global;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewHolder.relNoEt.setHint("请输入原交易参考号");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BANKCARD_MIS_REFUND) {
            if (resultCode == 0) {
                BankcardRefundResponse response = DataUtils.getRefundResponse(data);
                if (response.isSuccess()) {
                    savePaidInfo(response);
                } else if (response.isCancel()) {
                    showMessage("取消银行卡退款");
                } else {
                    String reason = response.getMessage();
                    showMessage("银行卡退款失败: " + (Strings.isBlank(reason) ? "" : reason));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    @Override
    protected void refund() {
        nav.enterMisRefundForResult(this, REQUEST_BANKCARD_MIS_REFUND, createRequest());
    }

    private BankcardRefundRequest createRequest() {
        String relNo = getRelNo();
        PaidInfo paidSource = refundDataManager.findRefundableOriByRelNo(relNo);
        BankcardRefundRequest request = new BankcardRefundRequest(type);
        if(type == 1){//当日撤销
            request.setTraceNo(getRelNo());
        }
        request.setAmount(paidSource.getSaleAmount().getAmount());
        request.setOperatorNo(global.getUserNo());
//        request.setOriDate();
        request.setOriReferenceNo(getRelNo());
        request.setOutOrderNo(refundDataManager.getSafeSaleNo());
        return request;
    }

    private void savePaidInfo(BankcardRefundResponse response) {
        PaidInfo paidSource = refundDataManager.findRefundableOriByRelNo(getRelNo());
        if (paidSource == null) {
            Toast.makeText(getContext(), "关联号错误", Toast.LENGTH_LONG).show();
            return;
        }
        PaidInfo refundPaid = new PaidInfo();
        refundPaid.setPaymentId(getPaymentId());
        refundPaid.setSaleAmount(paidSource.getSaleAmount());
        refundPaid.setPaymentName(getPaymentName());
        if (!TextUtils.isEmpty(response.getCardNo())) {
            refundPaid.setBillNo(response.getCardNo()); //银行卡号
        }
        if (!TextUtils.isEmpty(response.getReferenceNo())) {
            refundPaid.setCardNo(response.getReferenceNo());// 交易参考号
        }
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        refundSuccess(refundPaid);
    }
}
