package com.tianhe.pay.data.wechatali.refund;

import com.tianhe.pay.data.wechatali.TongguanRequest;

public class TongguanRefundRequest extends TongguanRequest {
    String lowOrderId;
    String upOrderId;

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
