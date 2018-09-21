package com.ali;

/**
 * 支付宝接口请求公共参数
 */
public class AliRequest {
    /**
     * 支付宝分配的应用ID
     */
    protected String app_id;
    /**
     * 交易接口名称
     *
     * @see com.ali.AliConstant.TradeName
     */
    protected String method;
    /**
     * 请求参数的格式. 仅支付JSON, 可以不填
     */
    protected String format;
    /**
     * 请求使用的编码格式
     * @see AliConstant.Charset
     */
    protected String charset;
    /**
     * 签名字符串使用的签名算法
     *
     * @see com.ali.AliConstant.SignType
     */
    protected String sign_type;
    /**
     * 通过签名算法计算后的字符串
     */
    protected String sign;
    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     */
    protected String timestamp;
    /**
     * 调用的接口版本
     *
     * @see com.ali.AliConstant.ApiVersion
     */
    protected String version;
    /**
     * 支付宝通知商户服务器的指定接口url
     */
    protected String notify_url;
    /**
     * 应用授权token
     */
    protected String app_auth_token;
    /**
     * 请求参数集合
     */
    protected String biz_content;

    public AliRequest() {
        this.app_id = AliConstant.APP_ID;
        this.format = AliConstant.BizContenFormat.JSON;
        this.charset = AliConstant.Charset.DEFAULT;
        this.version = AliConstant.ApiVersion.V1;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getApp_auth_token() {
        return app_auth_token;
    }

    public void setApp_auth_token(String app_auth_token) {
        this.app_auth_token = app_auth_token;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }
}
