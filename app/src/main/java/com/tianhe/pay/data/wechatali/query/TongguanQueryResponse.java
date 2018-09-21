package com.tianhe.pay.data.wechatali.query;

import com.tianhe.pay.data.wechatali.TongguanResponse;
import com.tianhe.pay.utils.money.Money;

public class TongguanQueryResponse extends TongguanResponse {
    String lowOrderId;
    String upOrderId;
    Money payMoney;
    String openid;    // 微信支付返回消费者在商户号appid下的唯一标识; 支付宝支付返回消费者支付宝账户名

    String payTime;

    public String getLowOrderId() {
        return lowOrderId;
    }

    public void setLowOrderId(String lowOrderId) {
        this.lowOrderId = lowOrderId;
    }

    public String getUpOrderId() {
        return upOrderId;
    }

    public void setUpOrderId(String upOrderId) {
        this.upOrderId = upOrderId;
    }

    public Money getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Money payMoney) {
        this.payMoney = payMoney;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
