package com.tencent.protocol.pay_protocol;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 扫用户授权码微信支付API参数封装
 */
public class ScanPayReqData {

    /**
     * 商户微信公众帐号ID(开通公众号之后可以获取到)
     */
    private String appid = "";
    /**
     * 微信商户号(开通公众号的微信支付功能之后可以获取到)
     */
    private String mch_id = "";
    /**
     * 设备号
     */
    private String device_info = "";
    /**
     * 随机字符串. 用于签名的动态salt
     */
    private String nonce_str = "";
    /**
     * 参数签名后的字符串
     */
    private String sign = "";
    /**
     * 商品描述. 购买者可以在微信支付成功信息中看到
     */
    private String body = "";
    /**
     * 附加数据. 自定义参数, 用来保存与交易相关的信息, 在查询API会原样返回.
     * 有助于商户自己可以注明该笔消费的具体内容, 方便后续的运营和记录.
     */
    private String attach = "";
    /**
     * 商户订单号(32个字符内可包含字母)
     */
    private String out_trade_no = "";
    /**
     * 账单总价(单位为"分")
     */
    private int total_fee = 0;
    /**
     * 商户终端设备IP
     */
    private String spbill_create_ip = "";
    /**
     * 交易开始时间(yyyyMMddHHmmss)
     */
    private String time_start = "";
    /**
     * 在微信平台配置的商品标记, 用于微信代金券/立减优惠活动
     */
    private String goods_tag = "";
    /**
     * 买家授权码.扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟.
     */
    private String auth_code = "";

    private ScanPayReqData(Builder builder) {
        setCommonParam(builder.appid, builder.mch_id);
        setDeviceParams(builder.device_info, builder.spbill_create_ip);
        setBizParams(builder.out_trade_no, builder.total_fee, builder.auth_code, builder.body, builder.time_start);
        setSaltAndSign();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private void setCommonParam(String appid, String mch_id) {
        setAppid(appid);
        setMch_id(mch_id);
    }

    private void setDeviceParams(String device_info, String spbill_create_ip) {
        setDevice_info(device_info);
        setSpbill_create_ip(spbill_create_ip);
    }

    private void setBizParams(String out_trade_no, int total_fee, String auth_code,String body, String time_start) {
        setOut_trade_no(out_trade_no);
        setAuth_code(auth_code);
        setTotal_fee(total_fee);
        setTime_start(time_start);
        setBody(body);
        setGoods_tag("");
    }

    private void setSaltAndSign() {
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        String sign = Signature.getSign(toMap());
        setSign(sign);
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

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
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
        private String device_info;
        private String nonce_str;
        private String sign;
        private String body;
        private String attach;
        private String out_trade_no;
        private int total_fee;
        private String spbill_create_ip;
        private String time_start;
        private String goods_tag;
        private String auth_code;

        private Builder() {
            setCommonParam(Configure.getAppid(), Configure.getMchid());
        }

        public Builder setCommonParam(String appid, String mchid) {
            this.appid = appid;
            this.mch_id = mchid;
            return this;
        }

        public Builder setDeviceParams(String deviceInfo, String ip) {
            this.device_info = deviceInfo;
            this.spbill_create_ip = ip;
            return this;
        }

        public Builder setBizParams(String outTradeNo, int totalFee, String barcode, String body, String timeStart) {
            this.out_trade_no = outTradeNo;
            this.total_fee = totalFee;
            this.auth_code = barcode;
            this.body = body;
            this.time_start = timeStart;
            return this;
        }

        public ScanPayReqData build() {
            return new ScanPayReqData(this);
        }


    }
}
