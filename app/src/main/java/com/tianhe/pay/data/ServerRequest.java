package com.tianhe.pay.data;

public class ServerRequest {
    private String sign;
    private String data;

    public ServerRequest(String sign, String data) {
        this.sign = sign;
        this.data = data;
    }
}
