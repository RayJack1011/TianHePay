package com.ali.demo.api.internal.parser.json;

import com.ali.demo.api.AlipayApiException;
import com.ali.demo.api.AlipayParser;
import com.ali.demo.api.AlipayRequest;
import com.ali.demo.api.AlipayResponse;
import com.ali.demo.api.SignItem;
import com.ali.demo.api.internal.mapping.Converter;

public class ObjectJsonParser<T extends AlipayResponse> implements AlipayParser<T> {
    private Class<T> clazz;

    public ObjectJsonParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws AlipayApiException {
        Converter converter = new JsonConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

    @Override
    public SignItem getSignItem(AlipayRequest<?> request, String responseBody)
            throws AlipayApiException {
        Converter converter = new JsonConverter();
        return converter.getSignItem(request, responseBody);
    }

    @Override
    public String encryptSourceData(AlipayRequest<?> request, String body, String format,
                                    String encryptType, String encryptKey, String charset)
            throws AlipayApiException {
        Converter converter = new JsonConverter();
        return converter.encryptSourceData(request, body, format,
                encryptType, encryptKey, charset);
    }
}
