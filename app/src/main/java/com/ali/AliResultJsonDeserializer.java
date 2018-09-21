package com.ali;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 支付宝返回Result数据的解析工具类.
 */
public class AliResultJsonDeserializer implements JsonDeserializer<AliResult> {
    @Override
    public AliResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            AliResult signedResponse = new AliResult();
            JsonObject jsonObject = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
                String key = elementEntry.getKey();
                if ("sign".equals(key)) {
                    signedResponse.setSign(elementEntry.getValue().getAsString());
                }
                // 支付宝当面付返回的json串中，data区域的key会根据date的类型不同有不同的key
                // key的规则是alipay_trade_xxx_response: 例如支付返回的date对应的key是alipay_trade_pay_response
                // 偷懒直接判断是否包含response
                else if (key.contains("response")) {
                    signedResponse.setResponseData(elementEntry.getValue().toString());
                }
            }
            return signedResponse;
        }
        return null;
    }
}
