package com.tianhe.pay.data.bankcard;

public class BankcardConstant {

    static class DataKey {
        static final String TRANS_TYPE = "transType";
        static final String OPERATOR_NO = "operatorNo";
        static final String OUT_ORDER_ID = "outOrderNo";
        static final String AMOUNT = "amount";
        static final String TRANS_AMOUNT = "transAmount";
        static final String TRANS_TIME = "transTime";
        static final String ORI_TRANS_TIME = "oriTransTime";
        static final String CARD_NO = "cardNo";
        static final String VOUCHER_NO = "voucherNo";
        static final String ORI_VOUCHER_NO = "oriVoucherNo";
        static final String BATCH_NO = "batchNo";
        static final String REFERENCE_NO = "referenceNo";
        static final String ORI_REFERENCE_NO = "oriReferenceNo";
        static final String ORI_DATE = "oriDate";
        static final String CHANNEL_ID = "channelId";
        static final String OPEN_ADMIN_VERIFY = "isOpenAdminVerify";
        static final String RESPONSE_CODE = "responseCode";
        static final String MESSAGE = "message";
        static final String PAY_TYPE = "payType";
    }

    /** 调用银行收单activity的ACTION */
    public static final String ACTION = "android.intent.action.NEWLAND.PAYMENT";
    /** 商户号 */
    public static final String MERCHANT_ID = "android.intent.action.NEWLAND.PAYMENT";
    /** 终端号 */
    public static final String POS_ID = "android.intent.action.NEWLAND.PAYMENT";

    /** 交易类型 */
    public static class TransactionType {
        /** 消费 */
        public static final int SALE = 2;
        /** 退货 */
        public static final int REFUND = 6;
        /** 消费撤销 */
        public static final int REVOKE = 7;
        /** 重打印 */
        public static final int REPINT = 9001;
    }

    /** 支付类型 */
    public static class PayType {
        /** 银行卡 */
        public static final int CARD = 1;
        /** 银联扫码 */
        public static final int SCAN = 2;
    }

    /** 渠道ID */
    public static class ChannelId {
        /** 银行卡 */
        public static final String ACQUIRE = "acquire";
        /** 会员卡 */
        public static final String VIP = "vipcard";
    }

    /** 应答码 */
    public static class ResponseCode {

        public static final String SUCCESS = "00";
        public static final String FAIL = "0";
        /** 交易失败或异常(一般在查询交易状态时可能返回) */
        public static final String ERROR = "EC";
        public static final String CANCEL = "QX";
    }

    /** 交易状态() */
    public static class TransactionStatus {
        /** 支付成功 */
        public static final String SUCCESS = "SUCCESS";
        /** 转入退款 */
        public static final String REFUND = "REFUND";
        /** 未支付 */
        public static final String NOTPAY = "NOTPAY";
        /** 已关闭 */
        public static final String CLOSED = "CLOSED";
        /** 已撤销 */
        public static final String REVOKED = "REVOKED";
    }
}
