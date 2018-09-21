package com.ali.demo.trade.service;

import com.ali.demo.api.response.AlipayTradeCancelResponse;
import com.ali.demo.trade.model.builder.AlipayTradeCancelRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.ali.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FPayResult;
import com.ali.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.ali.demo.trade.model.result.AlipayF2FQueryResult;
import com.ali.demo.trade.model.result.AlipayF2FRefundResult;

public interface AlipayTradeService {
    // 当面付2.0流程支付
    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder);

    // 当面付2.0撤销订单
    public AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelRequestBuilder builder);

    // 当面付2.0消费查询
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder);

    // 当面付2.0消费退款
    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder builder);

    // 当面付2.0预下单(生成二维码)
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder);
}
