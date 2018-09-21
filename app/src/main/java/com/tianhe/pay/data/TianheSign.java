package com.tianhe.pay.data;

public class TianheSign extends Md5Sign {

    private static String service_key = "hj234@sdfjj";

    @Override
    public String sign(String source) {
        return super.sign(source + service_key);
    }
}
