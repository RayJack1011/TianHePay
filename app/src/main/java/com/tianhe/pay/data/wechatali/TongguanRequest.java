package com.tianhe.pay.data.wechatali;

import com.tianhe.pay.data.Sign;

public abstract class TongguanRequest extends TongguanSignable {
    protected String account;
    @SkipSign
    protected String sign;  // 签名后的字符串

    public TongguanRequest() {
        this.account = TongguanConstant.ACCOUNT;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String signed(Sign sign) {
        this.sign = super.signed(sign);
        return this.sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
