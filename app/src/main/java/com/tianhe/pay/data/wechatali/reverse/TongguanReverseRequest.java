package com.tianhe.pay.data.wechatali.reverse;

import com.tianhe.pay.data.wechatali.TongguanRequest;

/**
 * 通莞金服(微信/支付宝)消费撤销请求封装. 支付成功15秒后，且不超过4分钟才能调用改接口
 */
public class TongguanReverseRequest extends TongguanRequest {
    String upOrderId;
    String lowOrderId;

    public String getUpOrderId() {
        return upOrderId;
    }

    public void setUpOrderId(String upOrderId) {
        this.upOrderId = upOrderId;
    }

    public String getLowOrderId() {
        return lowOrderId;
    }

    public void setLowOrderId(String lowOrderId) {
        this.lowOrderId = lowOrderId;
    }
}
