package com.tianhe.pay.ui.payment.storedvalue;

import android.app.Activity;
import android.content.Intent;

import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

public class StoredValueCardPayFragment extends PayFragment {
    private static final int REMAIN_AMOUNT_DIALOG_ID = BaseDialog.getAutoId();
    private static final int REQUEST_QUERY_CARD = 1001;

    private StoredValueCard card;
    private String pwd;
    private String track;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                card = (StoredValueCard) data.getSerializableExtra("data");
                pwd = data.getStringExtra("pwd");
                track = data.getStringExtra("track");
                checkRemaining();
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
        toQuery();
    }

    @Override
    public void onDialogOk(int dialogId) {
        if (dialogId == REMAIN_AMOUNT_DIALOG_ID) {
            paid(Money.createAsYuan(card.getRemainingAmount()));
        }
    }

    @Override
    public void onDialogCancel(int dialogId) {
        card = null;
        pwd = null;
    }

    private void toQuery() {
        nav.enterQueryCrmStoredValueCardForResult(this, REQUEST_QUERY_CARD);
    }

    private void checkRemaining() {
        Money remaining = Money.createAsYuan(card.getRemainingAmount());
        if (remaining.getAmount() < willPayCent) {
            MdDialog.Builder builder = new MdDialog.Builder(REMAIN_AMOUNT_DIALOG_ID);
            builder.title("储值卡余额不足");
            builder.message("当前余额" + card.getRemainingAmount() + ", 是否使用剩余余额?");
            builder.negativeText("取消");
            builder.positiveText("确定");
            showDialog(builder);
        } else {
            paid(new Money(willPayCent));
        }
    }

    private void paid(Money money) {
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(money);
        paidInfo.setBillNo(card.getCardNo());
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setPwd(pwd);
        paidInfo.setMarkCode(track.split("=")[1]);
        paidInfo.setTime(Times.nowDate());
        cartManager.addPaidInfo(paidInfo);
        getActivity().finish();
    }

}
