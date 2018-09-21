package com.tencent.protocol;

/**
 * 返回数据中的常量
 */
public class ResponseConstants {
    /**
     * 接口调用成功
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 接口调用失败(可能缺少参数)
     */
    public static final String FAIL = "FAIL";

    // error code
    /**
     * 授权码无效
     */
    public static final String AUTH_CODE_INVOLID = "AUTH_CODE_INVALID";
    /**
     * 授权码失效
     */
    public static final String AUTH_CODE_EXPIRE = "AUTHCODEEXPIRE";

    /**
     * 等待用户(输入密码)付款
     */
    public static final String USER_PAYING = "USERPAYING";
    /**
     * 付款用户余额不足
     */
    public static final String NOT_ENOUGH = "NOT_ENOUGH";

    // trade state
    /**
     * 支付成功
     */
    public static final String TRADE_SUCCESS = "SUCCESS";
    /**
     * 转入退款
     */
    public static final String TRADE_REFUND = "REFUND";
    /**
     * 未支付
     */
    public static final String TRADE_NOTPAY = "NOTPAY";
    /**
     * 已关闭
     */
    public static final String TRADE_CLOSED = "CLOSED";
    /**
     * 已撤销（刷卡支付）
     */
    public static final String TRADE_REVOKED = "REVOKED";
    /**
     * 用户支付中
     */
    public static final String TRADE_USER_PAYING = "USERPAYING";
    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    public static final String TRADE_PAY_ERROR = "PAYERROR";
}
