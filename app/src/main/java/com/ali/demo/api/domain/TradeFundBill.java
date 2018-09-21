package com.ali.demo.api.domain;

import com.ali.demo.api.AlipayObject;
import com.ali.demo.api.internal.mapping.ApiField;

/**
 * 交易支付的渠道属性
 */
public class TradeFundBill extends AlipayObject {
    /**
     * 该支付工具类型所使用的金额
     */
    @ApiField("amount")
    private String amount;

    /**
     * 交易使用的资金渠道，详见 <a href="https://doc.open.alipay.com/doc2/detail?treeId=26&articleId=103259&docType=1">支付渠道列表</a>
     */
    @ApiField("fund_channel")
    private String fundChannel;

    /**
     * 渠道实际付款金额
     */
    @ApiField("real_amount")
    private String realAmount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFundChannel() {
        return fundChannel;
    }

    public void setFundChannel(String fundChannel) {
        this.fundChannel = fundChannel;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }
}
