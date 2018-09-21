package com.tianhe.pay.data.wechatali;

/**
 * 通莞金服响应通用封装
 */
public abstract class TongguanResponse extends TongguanSignable {
    protected String status;    // 通莞金服响应交易请求的状态码
    protected String message;
    protected String account;
    protected String state;     // 通莞金服处理交易请求的实际状态
    @SkipSign
    protected String sign;      // 签名后的字符串

    public boolean isSuccess() {
        return TongguanConstant.CODE_SUCCESS.equals(status);
    }

    public boolean isTransactionSuccess() {
        return TongguanConstant.ORDER_STATE_SUCCESS.equals(state);
    }

    public boolean isTransactionFail() {
        return TongguanConstant.ORDER_STATE_FAIL.equals(state);
    }

    public boolean isCancel() {
        return TongguanConstant.ORDER_STATE_CANCEL.equals(state);
    }

    public boolean isWaitingCustomer() {
        return TongguanConstant.ORDER_STATE_WAITING_CUSTOMER.equals(state);
    }

    public boolean isForwardRefund() {
        return TongguanConstant.ORDER_STATE_REFUND.equals(state);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String stateName() {
        if (TongguanConstant.ORDER_STATE_SUCCESS.equals(state)) {
            return "交易成功";
        } else if (TongguanConstant.ORDER_STATE_FAIL.equals(state)) {
            return "交易失败";
        } else if (TongguanConstant.ORDER_STATE_CANCEL.equals(state)) {
            return "交易取消";
        } else if (TongguanConstant.ORDER_STATE_WAITING_CUSTOMER.equals(state)) {
            return "等待付款";
        } else if (TongguanConstant.ORDER_STATE_REFUND.equals(state)) {
            return "转入退款";
        }
        throw new IllegalStateException("无效的交易状态");
    }
}
