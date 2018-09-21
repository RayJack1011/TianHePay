package com.ali.demo.trade.service.impl;

import com.ali.demo.Configs;
import com.ali.demo.api.DefaultAlipayClient;
import com.ali.demo.api.internal.utils.StringUtils;
import com.ali.demo.api.request.AlipayTradePayRequest;
import com.ali.demo.api.response.AlipayTradePayResponse;
import com.ali.demo.api.response.AlipayTradeQueryResponse;
import com.ali.demo.trade.Constants;
import com.ali.demo.trade.model.TradeStatus;
import com.ali.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FPayResult;

public class AlipayTradeServiceImpl extends AbsAlipayTradeService {
    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;
        private String alipayPublicKey;
        private String signType;

        public AlipayTradeServiceImpl build() {
            if (StringUtils.isEmpty(gatewayUrl)) {
                // 与mcloudmonitor网关地址不同
                gatewayUrl = Configs.getOpenApiDomain();
            }
            if (StringUtils.isEmpty(appid)) {
                appid = Configs.getAppid();
            }
            if (StringUtils.isEmpty(privateKey)) {
                privateKey = Configs.getPrivateKey();
            }
            if (StringUtils.isEmpty(format)) {
                format = "json";
            }
            if (StringUtils.isEmpty(charset)) {
                charset = "utf-8";
            }
            if (StringUtils.isEmpty(alipayPublicKey)) {
                alipayPublicKey = Configs.getAlipayPublicKey();
            }
            if (StringUtils.isEmpty(signType)) {
                signType = Configs.getSignType();
            }

            return new AlipayTradeServiceImpl(this);
        }

        public ClientBuilder setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
            return this;
        }

        public ClientBuilder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public ClientBuilder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public ClientBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public ClientBuilder setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
            return this;
        }

        public ClientBuilder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public ClientBuilder setSignType(String signType) {
            this.signType = signType;
            return this;
        }

        public String getAlipayPublicKey() {
            return alipayPublicKey;
        }

        public String getSignType() {
            return signType;
        }

        public String getAppid() {
            return appid;
        }

        public String getCharset() {
            return charset;
        }

        public String getFormat() {
            return format;
        }

        public String getGatewayUrl() {
            return gatewayUrl;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }

    public AlipayTradeServiceImpl(ClientBuilder builder) {
        if (StringUtils.isEmpty(builder.getGatewayUrl())) {
            throw new NullPointerException("gatewayUrl should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAppid())) {
            throw new NullPointerException("appid should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getPrivateKey())) {
            throw new NullPointerException("privateKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getFormat())) {
            throw new NullPointerException("format should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getCharset())) {
            throw new NullPointerException("charset should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
            throw new NullPointerException("alipayPublicKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getSignType())) {
            throw new NullPointerException("signType should not be NULL!");
        }

        client = new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(),
                builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey(), builder.getSignType());
    }

    // 商户可以直接使用的pay方法
    @Override
    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder) {
        validateBuilder(builder);

        final String outTradeNo = builder.getOutTradeNo();

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        // 设置平台参数
        request.setNotifyUrl(builder.getNotifyUrl());
        String appAuthToken = builder.getAppAuthToken();
        // todo 等支付宝sdk升级公共参数后使用如下方法
        // request.setAppAuthToken(appAuthToken);
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());

        // 设置业务参数
        request.setBizContent(builder.toJsonString());

        // 首先调用支付api
        AlipayTradePayResponse response = (AlipayTradePayResponse) getResponse(client, request);

        AlipayF2FPayResult result = new AlipayF2FPayResult(response);
        if (response != null && Constants.SUCCESS.equals(response.getCode())) {
            // 支付交易明确成功
            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (response != null && Constants.PAYING.equals(response.getCode())) {
            result.setTradeStatus(TradeStatus.WAITING);

//            // 返回用户处理中，则轮询查询交易是否成功，如果查询超时，则调用撤销
//            AlipayTradeQueryRequestBuilder queryBuiler = new AlipayTradeQueryRequestBuilder()
//                    .setAppAuthToken(appAuthToken)
//                    .setOutTradeNo(outTradeNo);
//            AlipayTradeQueryResponse loopQueryResponse = loopQueryResult(queryBuiler);
//            return checkQueryAndCancel(outTradeNo, appAuthToken, result, loopQueryResponse);
        } else if (tradeError(response)) {
            // 设置状态未知, 让业务层去决定是否撤销
            result.setTradeStatus(TradeStatus.UNKNOWN);

//            // 系统错误，则查询一次交易，如果交易没有支付成功，则调用撤销
//            AlipayTradeQueryRequestBuilder queryBuiler = new AlipayTradeQueryRequestBuilder()
//                    .setAppAuthToken(appAuthToken)
//                    .setOutTradeNo(outTradeNo);
//            AlipayTradeQueryResponse queryResponse = tradeQuery(queryBuiler);
//            return checkQueryAndCancel(outTradeNo, appAuthToken, result, queryResponse);
        } else {
            // 其他情况表明该订单支付明确失败
            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }
}
