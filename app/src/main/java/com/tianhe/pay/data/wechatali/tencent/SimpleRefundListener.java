package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.business.RefundBusiness;
import com.tencent.protocol.refund_protocol.RefundResData;

public abstract class SimpleRefundListener implements RefundBusiness.ResultListener {
    @Override
    public void onFailByReturnCodeError(RefundResData refundResData) {
        onFail("参数错误1");
    }

    @Override
    public void onFailByReturnCodeFail(RefundResData refundResData) {
        onFail("参数错误2");
    }

    @Override
    public void onFailBySignInvalid(RefundResData refundResData) {
        onFail("返回数据签名验证失败");
    }

    @Override
    public void onRefundFail(RefundResData refundResData) {
        onFail("退款失败");
    }

    public abstract void onFail(String cause);
}
