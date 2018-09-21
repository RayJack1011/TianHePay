package com.tianhe.pay.ui.payment.bankcard;

import android.content.Intent;

import com.tianhe.pay.data.bankcard.DataUtils;
import com.tianhe.pay.data.bankcard.sale.BankcardSaleRequest;
import com.tianhe.pay.data.bankcard.sale.BankcardSaleResponse;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;

public class BankcardPayFragment extends PayFragment {
    private static final int REQUEST_BANKCARD_MIS = 1003;

    @Inject
    Global global;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BANKCARD_MIS) {
            // 银行返回的值比较特殊，0表示结果，不表示Activity.RESULT_CANCELED
            if (resultCode == 0) {
                BankcardSaleResponse response = DataUtils.getSaleResponse(data);
                if (response.isSuccess()) {
                    savePaidInfo(response);
                } else if (response.isCancel()) {
                    showMessage("取消支付");
                } else {
                    String reason = response.getMessage();
                    showMessage("支付失败: " + (Strings.isBlank(reason) ? "" : reason));
                }
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
    protected void pay() {
        nav.enterMisPayForResult(this, REQUEST_BANKCARD_MIS, createRequest());
    }

    private BankcardSaleRequest createRequest() {
        BankcardSaleRequest request = new BankcardSaleRequest();
        request.setAmount(willPayCent);
        request.setOperatorNo(global.getUserNo());
        request.setOutOrderNo(cartManager.getSafeSaleNo());
        return request;
    }

    private void savePaidInfo(BankcardSaleResponse response) {
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(new Money(willPayCent));
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setBillNo(response.getCardNo()); //银行卡号
        paidInfo.setCardNo(response.getReferenceNo());// 交易参考号
        paidInfo.setTime(Times.nowDate());
        cartManager.addPaidInfo(paidInfo);
        getActivity().finish();
    }
}
