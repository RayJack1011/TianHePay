package com.tianhe.pay.data.wechatali.refund;

import com.tianhe.pay.data.wechatali.TongguanResponse;

public class TongguanRefundResponse extends TongguanResponse {
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
