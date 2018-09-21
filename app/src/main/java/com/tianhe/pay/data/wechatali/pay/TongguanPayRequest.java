package com.tianhe.pay.data.wechatali.pay;

import com.tianhe.pay.data.wechatali.TongguanConstant;
import com.tianhe.pay.data.wechatali.TongguanRequest;
import com.tianhe.pay.utils.money.Money;

/**
 * 通莞金服(微信/支付宝)支付请求封装
 */
public class TongguanPayRequest extends TongguanRequest {

    Money payMoney;
    String lowOrderId;  // 天和订单号
    String barcode;     // 付款码
    String body;        // 商品描述(可以为null)
    String payType;     // 支付方式
    String lowCashier;  // 收银人员编号(可以为null)

    public Money getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Money payMoney) {
        this.payMoney = payMoney;
    }

    public String getLowOrderId() {
        return lowOrderId;
    }

    public void setLowOrderId(String lowOrderId) {
        this.lowOrderId = lowOrderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPayType() {
        return payType;
    }

    public String getLowCashier() {
        return lowCashier;
    }

    public void setLowCashier(String lowCashier) {
        this.lowCashier = lowCashier;
    }

    public void setWechatPayType() {
        this.payType = TongguanConstant.TYPE_WECHAT;
    }

    public void setAliType() {
        this.payType = TongguanConstant.TYPE_ALI;
    }

    public void setUnionPayType() {
        this.payType = TongguanConstant.TYPE_UNIONPAY;
    }
}
