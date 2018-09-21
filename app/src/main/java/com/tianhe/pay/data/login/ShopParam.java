package com.tianhe.pay.data.login;

import com.google.gson.annotations.SerializedName;

/**
 * 门店对应的参数
 */
public class ShopParam {
    /** 微信支付参数对应的编号 */
    private static final String PARAM_NO_WECHATALI_ACCOUNT = "01";
    private static final String PARAM_NO_WECHATALI_KEY = "02";

    @SerializedName("PARM_NO_")
    String paramNo;                     // 参数编号
    @SerializedName("PARM_VAL_")
    String paramValue;                  // 参数值
    @SerializedName("PARM_DESC_")
    String paramDescription;            // 参数描述

    public String getParamNo() {
        return paramNo;
    }

    public void setParamNo(String paramNo) {
        this.paramNo = paramNo;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    public boolean isWechatAliAccount() {
        return PARAM_NO_WECHATALI_ACCOUNT.equals(paramNo);
    }

    public boolean isWechatAliKey() {
        return PARAM_NO_WECHATALI_KEY.equals(paramNo);
    }
}
