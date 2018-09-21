package com.tencent.protocol.refund_protocol;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *  微信退货API参数封装.
 *  transaction_id 、out_trade_no 二选一, 同时存在时优先级：transaction_id > out_trade_no
 */
public class RefundReqData {

    /**
     * 商户微信公众帐号ID(开通公众号之后可以获取到)
     */
    private String appid = "";
    /**
     * 微信商户号(开通公众号的微信支付功能之后可以获取到)
     */
    private String mch_id = "";
    /**
     * 随机字符串. 用于签名的动态salt
     */
    private String nonce_str = "";
    /**
     * 参数签名后的字符串
     */
    private String sign = "";
    /**
     * 微信系统原交易单号
     */
    private String transaction_id = "";
    /**
     * 商户原交易单号
     */
    private String out_trade_no = "";
    /**
     * 商户退货交易单号
     */
    private String out_refund_no = "";
    /**
     * 原交易总额
     */
    private int total_fee = 0;
    /**
     * 退货金额
     */
    private int refund_fee = 0;
    /**
     * 退货货币类型
     */
    private String refund_fee_type = "CNY";

    private RefundReqData(Builder builder) {
        setCommonParams(builder.appid, builder.mch_id);
        setBizParams(builder.transaction_id, builder.out_trade_no, builder.out_refund_no,
                builder.total_fee, builder.refund_fee);
        setSaltAndSign();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void setSaltAndSign() {
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        String sign = Signature.getSign(toMap());
        setSign(sign);
    }

    private void setBizParams(String transactionID, String outTradeNo, String outRefundNo,
                              int totalFee, int refundFee) {
        setTransaction_id(transactionID);
        setOut_trade_no(outTradeNo);
        setOut_refund_no(outRefundNo);
        setTotal_fee(totalFee);
        setRefund_fee(refundFee);
    }

    private void setCommonParams(String appId, String mchId) {
        setAppid(appId);
        setMch_id(mchId);
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public void setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static final class Builder {
        private String appid;
        private String mch_id;
        private String transaction_id;
        private String out_trade_no;
        private String out_refund_no;
        private int total_fee;
        private int refund_fee;

        private Builder() {
            setCommonParams(Configure.getAppid(), Configure.getMchid());
        }

        public Builder setCommonParams(String appId, String mchId) {
            this.appid = Configure.getAppid();
            this.mch_id = Configure.getMchid();
            return this;
        }

        public Builder setBizParams(String transactionID, String outTradeNo, String outRefundNo,
                                    int totalFee, int refundFee) {
            this.transaction_id = transactionID;
            this.out_trade_no = outTradeNo;
            this.out_refund_no = outRefundNo;
            this.total_fee = totalFee;
            this.refund_fee = refundFee;
            return this;
        }

        public RefundReqData build() {
            return new RefundReqData(this);
        }
    }
}
