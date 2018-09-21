package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.business.ScanPayBusiness;
import com.tencent.protocol.pay_protocol.ScanPayResData;

public abstract class SimpleScanPayListener implements ScanPayBusiness.ResultListener {

    @Override
    public void onFailByReturnCodeError(ScanPayResData scanPayResData) {
        onFail("参数错误1");
    }

    @Override
    public void onFailByReturnCodeFail(ScanPayResData scanPayResData) {
        onFail("参数错误2");
    }

    @Override
    public void onFailBySignInvalid(ScanPayResData scanPayResData) {
        onFail("返回数据签名验证失败");
    }

    @Override
    public void onFailByAuthCodeExpire(ScanPayResData scanPayResData) {
        onFail("二维码已过期");
    }

    @Override
    public void onFailByAuthCodeInvalid(ScanPayResData scanPayResData) {
        onFail("授权码无效");
    }

    @Override
    public void onFailByMoneyNotEnough(ScanPayResData scanPayResData) {
        onFail("余额不足");
    }

    @Override
    public void onFail(ScanPayResData scanPayResData) {
        onFail("交易shib");
    }


    protected abstract void onFail(String cause);
}
