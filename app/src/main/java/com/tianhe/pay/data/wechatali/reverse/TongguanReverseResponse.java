package com.tianhe.pay.data.wechatali.reverse;

import com.tianhe.pay.data.wechatali.TongguanResponse;

/**
 *  通莞金服(微信/支付宝)消费撤销响应封装.
 */
public class TongguanReverseResponse extends TongguanResponse {
    String upOrderId;
    String lowOrderId;
    String state;
}
