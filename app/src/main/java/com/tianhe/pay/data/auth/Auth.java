package com.tianhe.pay.data.auth;

import java.io.Serializable;

/** 权限 */
public class Auth implements Serializable {
    /** 退货权限 */
    public static final String REFUND_ORDER = "01";
    /** 微信退款 */
    public static final String REFUND_WECHAT = "02";

    private String authString;

    public String[] getAuthFlags() {
        if (authString == null) {
            return null;
        }
        return authString.split(",");
    }

    public boolean hasRefundAuth() {
        String[] authFlags = getAuthFlags();
        if (authFlags == null) {
            return false;
        }
        for (String s : authFlags) {
            if (REFUND_ORDER.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasReprintAuth() {
        return hasRefundAuth();
    }

}
