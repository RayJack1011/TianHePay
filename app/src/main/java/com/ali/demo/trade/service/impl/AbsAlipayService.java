package com.ali.demo.trade.service.impl;

import com.ali.demo.api.AlipayApiException;
import com.ali.demo.api.AlipayClient;
import com.ali.demo.api.AlipayRequest;
import com.ali.demo.api.AlipayResponse;
import com.ali.demo.trade.model.builder.RequestBuilder;

abstract class AbsAlipayService {
    // 验证bizContent对象
    protected void validateBuilder(RequestBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("builder should not be NULL!");
        }

        if (!builder.validate()) {
            throw new IllegalStateException("builder validate failed! " + builder.toString());
        }
    }

    // 调用AlipayClient的execute方法，进行远程调用
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
        try {
            AlipayResponse response = client.execute(request);
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
