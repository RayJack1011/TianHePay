package com.ali;

/**
 * 支付宝返回结果.
 * responseData是接口返回的业务结果, {@link AliResponse}的JSON格式;
 * sign是业务结果的签名
 */
public class AliResult {
    /**
     * 业务结果数据数据签名后的字符串
     */
    private String sign;
    /**
     * 业务结果数据
     */
    private String responseData;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

}
