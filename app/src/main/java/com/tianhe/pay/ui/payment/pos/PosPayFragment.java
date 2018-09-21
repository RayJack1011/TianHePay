package com.tianhe.pay.ui.payment.pos;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

/**
 * 离线支付(微信离线/支付宝离线/银行卡离线)
 */
public class PosPayFragment extends PayFragment {

    @Override
    protected boolean needRelNo() {
        return true;
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

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
