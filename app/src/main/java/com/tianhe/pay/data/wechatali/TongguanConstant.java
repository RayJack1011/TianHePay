package com.tianhe.pay.data.wechatali;

public class TongguanConstant {

    // 测试帐号
    private static final String TEST_ACCOUNT = "13974747474";
    private static final String TEST_KEY = "5f61d7f65b184d19a1e006bc9bfb6b2f";

    /**
     * 通莞金服(微信/支付包)商户号
     */
    public static final String ACCOUNT = TEST_ACCOUNT;
    public static final String SIGN_KEY = TEST_KEY;

    // 请求的状态码. 请求状态为CODE_SUCCESS只表明与通莞金服网络连接成功
    public static final String CODE_SUCCESS = "100";
    public static final String CODE_FAIL = "101";
    public static final String CODE_BARCODE_USED = "104"; // 付款码已被使用, 不能被重复使用.

    // 交易状态. 只有请求码是CODE_SUCCESS, 才能有交易状态返回
    /** 交易成功 */
    public static final String ORDER_STATE_SUCCESS = "0";
    /** 交易失败 */
    public static final String ORDER_STATE_FAIL = "1";
    /** 交易取消 */
    public static final String ORDER_STATE_CANCEL = "2";
    /** (支付交易中)等待用户支付 */
    public static final String ORDER_STATE_WAITING_CUSTOMER = "4";
    /** 转入退款 */
    public static final String ORDER_STATE_REFUND = "5";

    // 通莞金服支持的支付方式
    public static final String TYPE_WECHAT = "0";
    public static final String TYPE_ALI = "1";
    public static final String TYPE_UNIONPAY = "2";

    public static final String URL_PAY = "http://tgjf.833006.biz/tgPosp/services/payApi/micropay";
    public static final String URL_REVERSE = "http://tgjf.833006.biz/tgPosp/services/payApi/reverseImmediately";
    public static final String URL_REFUND = "http://tgjf.833006.biz/tgPosp/services/payApi/reverse";
    public static final String URL_QUERY = "http://tgjf.833006.biz/tgPosp/services/payApi/orderQuery";

}
