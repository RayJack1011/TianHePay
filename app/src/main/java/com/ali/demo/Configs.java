package com.ali.demo;

import com.ali.AliConstant;
import com.tianhe.pay.utils.Strings;

public class Configs {
    private static String openApiDomain;   // 支付宝openapi域名
    private static String mcloudApiDomain;  // 支付宝mcloudmonitor域名
    private static String pid;             // 商户partner id
    private static String appid;           // 商户应用id

    private static String privateKey;      // RSA私钥，用于对商户请求报文加签
    private static String publicKey;       // RSA公钥，仅用于验证开发者网关
    private static String alipayPublicKey; // 支付宝RSA公钥，用于验签支付宝应答
    private static String signType;     // 签名类型

    private static int maxQueryRetry;   // 最大查询次数
    private static long queryDuration;  // 查询间隔（毫秒）

    private static int maxCancelRetry;  // 最大撤销次数
    private static long cancelDuration; // 撤销间隔（毫秒）

    private static long heartbeatDelay ; // 交易保障线程第一次调度延迟（秒）
    private static long heartbeatDuration ; // 交易保障线程调度间隔（秒）

    private Configs() {
        // No Constructor
    }

    // 根据文件名读取配置文件，文件后缀名必须为.properties
    public synchronized static void init() {
        openApiDomain = AliConstant.API_URL;
        mcloudApiDomain = "";

        pid = AliConstant.PID;
        appid = AliConstant.APP_ID;

        // RSA
        privateKey = AliConstant.PRIVATE_KEY;
        publicKey = AliConstant.PUBLIC_KEY;
        alipayPublicKey = AliConstant.ALI_PUBLIC_KEY;
        signType = AliConstant.SignType.RSA2;

        // 查询参数
        maxQueryRetry = AliConstant.MAX_QUERY_RETRY;
        queryDuration = AliConstant.QUERY_DURATION;
        maxCancelRetry = AliConstant.MAX_CANCEL_RETRY;
        cancelDuration = AliConstant.CANCEL_DURATION;
    }

    public static String description() {
        StringBuilder sb = new StringBuilder("Configs{");
        sb.append("支付宝openapi网关: ").append(openApiDomain).append("\n");
        if (Strings.isNotEmpty(mcloudApiDomain)) {
            sb.append(", 支付宝mcloudapi网关域名: ").append(mcloudApiDomain).append("\n");
        }

        if (Strings.isNotEmpty(pid)) {
            sb.append(", pid: ").append(pid).append("\n");
        }
        sb.append(", appid: ").append(appid).append("\n");

        sb.append(", 商户RSA私钥: ").append(getKeyDescription(privateKey)).append("\n");
        sb.append(", 商户RSA公钥: ").append(getKeyDescription(publicKey)).append("\n");
        sb.append(", 支付宝RSA公钥: ").append(getKeyDescription(alipayPublicKey)).append("\n");
        sb.append(", 签名类型: ").append(signType).append("\n");

        sb.append(", 查询重试次数: ").append(maxQueryRetry).append("\n");
        sb.append(", 查询间隔(毫秒): ").append(queryDuration).append("\n");
        sb.append(", 撤销尝试次数: ").append(maxCancelRetry).append("\n");
        sb.append(", 撤销重试间隔(毫秒): ").append(cancelDuration).append("\n");

        sb.append(", 交易保障调度延迟(秒): ").append(heartbeatDelay).append("\n");
        sb.append(", 交易保障调度间隔(秒): ").append(heartbeatDuration).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String getKeyDescription(String key) {
        int showLength = 6;
        if (Strings.isNotEmpty(key) &&
                key.length() > showLength) {
            return new StringBuilder(key.substring(0, showLength))
                    .append("******")
                    .append(key.substring(key.length() - showLength))
                    .toString();
        }
        return null;
    }

    public static String getOpenApiDomain() {
        return openApiDomain;
    }

    public static String getMcloudApiDomain() {
        return mcloudApiDomain;
    }

    public static void setMcloudApiDomain(String mcloudApiDomain) {
        Configs.mcloudApiDomain = mcloudApiDomain;
    }

    public static String getPid() {
        return pid;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public static String getSignType() {
        return signType;
    }

    public static int getMaxQueryRetry() {
        return maxQueryRetry;
    }

    public static long getQueryDuration() {
        return queryDuration;
    }

    public static int getMaxCancelRetry() {
        return maxCancelRetry;
    }

    public static long getCancelDuration() {
        return cancelDuration;
    }

    public static void setOpenApiDomain(String openApiDomain) {
        Configs.openApiDomain = openApiDomain;
    }

    public static void setPid(String pid) {
        Configs.pid = pid;
    }

    public static void setAppid(String appid) {
        Configs.appid = appid;
    }

    public static void setPrivateKey(String privateKey) {
        Configs.privateKey = privateKey;
    }

    public static void setPublicKey(String publicKey) {
        Configs.publicKey = publicKey;
    }

    public static void setAlipayPublicKey(String alipayPublicKey) {
        Configs.alipayPublicKey = alipayPublicKey;
    }

    public static void setSignType(String signType) {
        Configs.signType = signType;
    }

    public static void setMaxQueryRetry(int maxQueryRetry) {
        Configs.maxQueryRetry = maxQueryRetry;
    }

    public static void setQueryDuration(long queryDuration) {
        Configs.queryDuration = queryDuration;
    }

    public static void setMaxCancelRetry(int maxCancelRetry) {
        Configs.maxCancelRetry = maxCancelRetry;
    }

    public static void setCancelDuration(long cancelDuration) {
        Configs.cancelDuration = cancelDuration;
    }

    public static long getHeartbeatDelay() {
        return heartbeatDelay;
    }

    public static void setHeartbeatDelay(long heartbeatDelay) {
        Configs.heartbeatDelay = heartbeatDelay;
    }

    public static long getHeartbeatDuration() {
        return heartbeatDuration;
    }

    public static void setHeartbeatDuration(long heartbeatDuration) {
        Configs.heartbeatDuration = heartbeatDuration;
    }
}
