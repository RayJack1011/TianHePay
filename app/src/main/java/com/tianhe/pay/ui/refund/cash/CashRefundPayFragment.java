package com.tianhe.pay.ui.refund.cash;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class CashRefundPayFragment extends RefundPayFragment {

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    @Override
    protected boolean needRelNo() {
        return false;
    }

    @Override
    protected boolean canModifyRefundAmount() {
        return true;
    }

    @Override
    protected void refund() {
        String refunded = getModifyAmount();

        PaidInfo refundPaid = new PaidInfo();
        refundPaid.setPaymentId(getPaymentId());
        refundPaid.setSaleAmount(Money.createAsYuan(refunded));
        refundPaid.setPaymentName(getPaymentName());
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        refundSuccess(refundPaid);
    }

}
