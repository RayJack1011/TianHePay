package com.tianhe.pay.ui.payment.coupon;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.ui.payment.PayFragment;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import java.math.BigDecimal;

public class CouponPayFragment extends PayFragment {
    private static final int REMAIN_AMOUNT_DIALOG_ID = BaseDialog.getAutoId();
    private static final int REQUEST_QUERY_COUPON = 1002;

    Coupon coupon;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY_COUPON) {
            if (resultCode == Activity.RESULT_OK) {
                coupon = (Coupon) data.getSerializableExtra("data");
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
            paid(Money.createAsYuan(coupon.getAmount()));
        }
    }

    private void toQuery() {
        nav.enterQueryCrmCouponForResult(this, REQUEST_QUERY_COUPON);
    }
    BigDecimal couponLimitMoney;
    private void checkRemaining() {
        String couponType = coupon.getCouponType();
        String amount = coupon.getAmount();
        BigDecimal money = new BigDecimal(amount);
//        Money remaining = Money.createAsYuan(coupon.getAmount());
        couponLimitMoney = CommomData.coupopLimitMap.get(couponType);
//        couponLimitMoney = new BigDecimal(100);
        Log.e("qqq",couponLimitMoney+"::::::::::;quan前");
        if(couponLimitMoney!=null){
            if(couponLimitMoney.compareTo(money)>=0){
                //表示可以接
                Money remaining = Money.createAsYuan(coupon.getAmount());
                if(remaining.getAmount() != willPayCent){
                    Toast.makeText(getActivity(),"输入的券金额错误",Toast.LENGTH_LONG).show();
                }else{
                    paid(remaining);
                }
            }else{
                //超过了商品的衔接金额
                Toast.makeText(getActivity(),"已到最大优惠",Toast.LENGTH_LONG).show();
            }
        }else{
            //商品不能借改权重
            Toast.makeText(getActivity(),"不能使用该券!",Toast.LENGTH_LONG).show();
        }
//        if (remaining.getAmount() < willPayCent) {
//            MdDialog.Builder builder = new MdDialog.Builder(REMAIN_AMOUNT_DIALOG_ID);
//            builder.title("券金额不足");
//            builder.message("当前余额: " + remaining.toString() + ", 是否使用券全额支付");
//            builder.negativeText("取消");
//            builder.positiveText("确定");
//            showDialog(builder);
//        } else {
//            paid(new Money(willPayCent));
//        }


    }

    private void paid(Money money) {
        PaidInfo paidInfo = new PaidInfo();
        paidInfo.setPaymentId(getPaymentId());
        paidInfo.setSaleAmount(money);
        couponLimitMoney = couponLimitMoney.subtract(new BigDecimal(coupon.getAmount()));
        Log.e("qqq",couponLimitMoney+"::::::::::;quanhou");
        CommomData.coupopLimitMap.put(coupon.getCouponType(),couponLimitMoney);
        paidInfo.setBillNo(coupon.getCouponNo());
        paidInfo.setPaymentName(getPaymentName());
        paidInfo.setTime(Times.nowDate());
        paidInfo.setCttype(coupon.getCouponType());
        cartManager.addPaidInfo(paidInfo);
        getActivity().finish();
    }
}
