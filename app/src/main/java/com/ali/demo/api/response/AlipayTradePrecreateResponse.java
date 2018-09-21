package com.ali.demo.api.response;

import com.ali.demo.api.AlipayResponse;
import com.ali.demo.api.internal.mapping.ApiField;

/**
 * 统一收单线下交易预创建（扫码支付）返回的业务数据
 */
public class AlipayTradePrecreateResponse extends AlipayResponse {
    /**
     * 商户的订单号
     */
    @ApiField("out_trade_no")
    private String outTradeNo;

    /**
     * 当前预下单请求生成的二维码码串，可以用二维码生成工具根据该码串值生成对应的二维码
     */
    @ApiField("qr_code")
    private String qrCode;

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    public String getOutTradeNo( ) {
        return this.outTradeNo;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    public String getQrCode( ) {
        return this.qrCode;
    }
}
