package com.tianhe.pay.ui.refund.pos;

import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.ui.refund.RefundPayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class PosRefundPayFragment extends RefundPayFragment {

    @Override
    protected boolean canModifyRefundAmount() {
        return true;
    }

    @Override
    protected void refund() {
//        PaidInfo paidSource = refundDataManager.findRefundableOriByRelNo(getRelNo());

        PaidInfo refundPaid = new PaidInfo();
        refundPaid.setPaymentId(getPaymentId());
        refundPaid.setSaleAmount(Money.createAsYuan(getModifyAmount()));
        refundPaid.setPaymentName(getPaymentName());
        refundPaid.setBillNo(getRelNo()); // 交易参考号
        refundPaid.setTime(Times.nowDate());
        refundDataManager.addRefundPaid(refundPaid);
        refundSuccess(refundPaid);
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

}
