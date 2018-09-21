package com.tianhe.pay.ui.refund.storedvalue;

import android.app.Activity;
import android.content.Intent;

import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class StoredValueCardRefundPayFragment extends RefundPayFragment {
    private static final int REQUEST_QUERY_CARD = 1001;

    private String pwd;
    private String track;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                StoredValueCard card = (StoredValueCard) data.getSerializableExtra("data");
                pwd = data.getStringExtra("pwd");
                track = data.getStringExtra("track");
                refundPaid(card);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    @Override
    protected boolean canModifyRefundAmount() {
        return true;
    }

    @Override
    protected void refund() {
        toQuery();
    }

    @Override
    protected boolean needRelNo() {
        return false;
    }

    private void toQuery() {
        nav.enterQueryCrmStoredValueCardForResult(this, REQUEST_QUERY_CARD);
    }

    private void refundPaid(StoredValueCard card) {
        String refunded = getModifyAmount();

        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(Money.createAsYuan(refunded));
        paidInfo.setBillNo(card.getCardNo());
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setPwd(pwd);
        paidInfo.setMarkCode(track.split("=")[1]);
        paidInfo.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(paidInfo);
        refundSuccess(paidInfo);
    }
}
