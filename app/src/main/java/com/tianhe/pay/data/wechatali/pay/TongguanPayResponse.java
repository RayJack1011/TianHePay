package com.tianhe.pay.data.wechatali.pay;

import com.tianhe.pay.data.Signable;
import com.tianhe.pay.data.wechatali.TongguanResponse;
import com.tianhe.pay.utils.money.Money;

/**
 * 通莞金服(微信/支付宝)支付响应封装
 */
public class TongguanPayResponse extends TongguanResponse implements Signable {

    String lowOrderId;  // 天和订单号
    Money payMoney;
    String upOrderId;   // 通莞金服订单号
    String payTime;     // 支付完成时间
    String openid;      // 微信支付返回消费者在商户号appid下的唯一标识; 支付宝支付返回消费者支付宝账户名

    public String getLowOrderId() {
        return lowOrderId;
    }

    public void setLowOrderId(String lowOrderId) {
        this.lowOrderId = lowOrderId;
    }

    public Money getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Money payMoney) {
        this.payMoney = payMoney;
    }

    public String getUpOrderId() {
        return upOrderId;
    }

    public void setUpOrderId(String upOrderId) {
        this.upOrderId = upOrderId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
