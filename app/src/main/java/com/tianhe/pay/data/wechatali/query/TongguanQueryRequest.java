package com.tianhe.pay.data.wechatali.query;

import com.tianhe.pay.data.wechatali.TongguanRequest;

/**
 * 通莞金服(微信/支付宝)查询请求封装
 */
public class TongguanQueryRequest extends TongguanRequest {
    String lowOrderId;  // 天和订单号
    String upOrderId;   // 通莞金服订单号

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
}
