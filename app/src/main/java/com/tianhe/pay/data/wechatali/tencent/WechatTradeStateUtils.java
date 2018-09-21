package com.tianhe.pay.data.wechatali.tencent;

public class WechatTradeStateUtils {
    //    SUCCESS--支付成功
    //    REFUND--转入退款
    //    NOTPAY--未支付
    //    CLOSED--已关闭
    //    REVOKED--已撤销
    //    USERPAYING--用户支付中
    //    NOPAY--未支付(确认支付超时)
    //    PAYERROR--支付失败(其他原因，
    //            如银行返回失败)
    public static String mapper(String tradeState) {
        if("SUCCESS".equals(tradeState)) {
            return "支付成功";
        }
        if("REFUND".equals(tradeState)) {
            return "转入退款";
        }
        if("NOTPAY".equals(tradeState)) {
            return "未支付";
        }
        if("CLOSED".equals(tradeState)) {
            return "已关闭";
        }
        if("REVOKED".equals(tradeState)) {
            return "已撤销";
        }
        if("USERPAYING".equals(tradeState)) {
            return "用户支付中";
        }
        if("NOPAY".equals(tradeState)) {
            return "未支付(支付超时)";
        }
        if("PAYERROR".equals(tradeState)) {
            return "支付失败";
        }
        return null;
    }

    public static boolean refundable(String tradeState) {
        return "SUCCESS".equals(tradeState);
    }
}
