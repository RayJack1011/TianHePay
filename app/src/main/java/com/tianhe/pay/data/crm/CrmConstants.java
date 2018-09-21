package com.tianhe.pay.data.crm;

public class CrmConstants {

    public static final String URL = "http://172.169.10.86:9999/IntegrationEntry?WSDL";
    public static final String NAMESPACE = "http://entry.serviceengine.cross.digiwin.com";
    public static final String ACTION = "invokeSrv";
    public static final int TIEMOUT = 30000;

    // server name
    public static final String QUERY_MEMBER = "MemberInfoGet";
    public static final String QUERY_SINGLE_CARD = "SingleCardGet";
    public static final String QUERY_SINGLE_COUPON = "SingleCouponGet";
    public static final String PROCESS_MEMBER_POINT = "MemberPointProcess";
    public static final String PROCESS_PAYMENT = "PaymentProcess";
    public static final String GIFT_PROCESS = "GiftProcess";

    // mode 当有数据写入CRM系统时(积分处理(增加/扣减)/付款处理/赠送卡券), 需要进行模式选择
    public static final String MODE_NORMAL = "0";
    public static final String MODE_TRAINING = "1";

    // crm接口服务按照正常流程执行(但不一定成功, 例如扣减积分服务正常返回,但是扣减结果失败)
    public static final String SUCCESS_SRVCODE = "000";
    // crm接口服务正常返回时, 请求服务成功
    public static final String SUCCESS_STATUS_CODE = "0";

    // Crm数据变更类型
    /** 因为提交销售单而变更数据 */
    public static final String TYPE_SALE = "1";
    /** 因为充值行为而变更数据 */
    public static final String TYPE_RECHARGE = "2";
    /** 积分异动类型:销售单 */
    public static final String TYPE_POINT_FOR_SALE = "A";

    /** 券流转状态: 发售 */
    public static final String STATE_COUPON_CREATE = "4";
    /** 券流转状态: 发售退回 */
    public static final String STATE_COUPON_REFUND = "5";
    /** 券流转状态: 已使用 */
    public static final String STATE_COUPON_USED = "6";
}
