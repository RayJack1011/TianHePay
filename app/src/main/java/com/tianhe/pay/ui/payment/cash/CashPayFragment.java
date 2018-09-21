package com.tianhe.pay.ui.payment.cash;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class CashPayFragment extends PayFragment {

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    @Override
    protected boolean needRelNo() {
        return false;
    }

    @Override
    protected void pay() {
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(new Money(willPayCent));
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setTime(Times.nowDate());
        cartManager.addPaidInfo(paidInfo);
        getActivity().finish();
    }

}
