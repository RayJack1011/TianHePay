package com.ali;

public class AlipayTradeStates {
    public static String stateName(String tradeStatus) {
        if (tradeStatus == null) {
            return "未知交易状态";
        }
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            return "支付成功";
        } else if ("TRADE_CLOSED".equals(tradeStatus)) {
            return "已退款";
        } else if ("TRADE_FINISHED".equals(tradeStatus)) {
            return "交易结束(不可退款)";
        } else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
            return "等待买家付款";
        }
        return null;
    }
}
