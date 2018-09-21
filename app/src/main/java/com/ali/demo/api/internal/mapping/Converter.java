package com.ali.demo.api.internal.mapping;

import com.ali.demo.api.AlipayApiException;
import com.ali.demo.api.AlipayRequest;
import com.ali.demo.api.AlipayResponse;
import com.ali.demo.api.SignItem;

/**
 * 动态格式转换器。
 */
public interface Converter {

    /**
     * 把字符串转换为响应对象。
     *
     * @param <T>   领域泛型
     * @param rsp   响应字符串
     * @param clazz 领域类型
     * @return 响应对象
     * @throws AlipayApiException
     */
    public <T extends AlipayResponse> T toResponse(String rsp, Class<T> clazz)
            throws AlipayApiException;

    /**
     * 获取响应内的签名数据
     *
     * @param request
     * @param responseBody
     * @return
     * @throws AlipayApiException
     */
    public SignItem getSignItem(AlipayRequest<?> request, String responseBody)
            throws AlipayApiException;

    /**
     * 获取解密后的响应内的真实内容
     *
     * @param request
     * @param body
     * @param format
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws AlipayApiException
     */
    public String encryptSourceData(AlipayRequest<?> request, String body, String format,
                                    String encryptType, String encryptKey, String charset)
            throws AlipayApiException;

}
