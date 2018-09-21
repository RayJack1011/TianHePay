package com.ali;

/**
 * 交易的资金渠道信息
 */
public class TradeFundBill {
    /**
     * 支付渠道
     * @see AliConstant.FundChannels
     */
    private String fund_channel;
    /**
     * 银行卡支付时的银行代码. 例如CEB
     */
    private String bank_code;

    /**
     * 支付金额
     */
    private String amount;
    /**
     * 该渠道实付金额
     */
    private String real_amount;

    /**
     * 渠道所使用的资金类型.
     * (fund_channel == AliConstant.FundChannels.BANKCARD)才有该字段.
     * @see AliConstant.FundBankcardType
     */
    private String fund_type;

    public String getFund_channel() {
        return fund_channel;
    }

    public void setFund_channel(String fund_channel) {
        this.fund_channel = fund_channel;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(String real_amount) {
        this.real_amount = real_amount;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }
}
