package com.tencent.protocol.reverse_protocol;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信撤销订单API参数封装.
 * 支付失败/超时, 等异常情况出现时. 调用撤销可以减少单边账的情况
 */
public class ReverseReqData {
    /**
     * 微信分配的公众号ID（开通公众号之后可以获取到）
     */
    private String appid = "";
    /**
     * 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
     */
    private String mch_id = "";
    /**
     * 微信系统交易单号
     */
    private String transaction_id = "";
    /**
     * 商户交易单号
     */
    private String out_trade_no = "";
    /**
     * 随机字符串. 用于签名的动态salt
     */
    private String nonce_str = "";
    /**
     * 参数签名后的字符串
     */
    private String sign = "";

    public ReverseReqData(String transactionID, String outTradeNo){
        setCommonParams();
        setBizParams(transactionID, outTradeNo);
        setSaltAndSign();
    }

    private void setSaltAndSign() {
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        String sign = Signature.getSign(toMap());
        setSign(sign);
    }

    private void setBizParams(String transactionID, String outTradeNo) {
        setTransaction_id(transactionID);
        setOut_trade_no(outTradeNo);
    }

    private void setCommonParams() {
        setAppid(Configure.getAppid());
        setMch_id(Configure.getMchid());
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

}
