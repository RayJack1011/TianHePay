package com.tianhe.pay.data;

public class ServerResult<DATA> {
    private static final String CODE_ON_SUCCESS = "0";
    private static final String CODE_ON_FAIL = "1";

    private String code;
    private String msg;
    private DATA data;

    public static <DATA> ServerResult<DATA> success(DATA data) {
        return new ServerResult<>(CODE_ON_SUCCESS, null, data);
    }

    public static <DATA> ServerResult<DATA> failed(String errorMessage) {
        return new ServerResult<>(CODE_ON_FAIL, errorMessage, null);
    }

    public ServerResult(String code, String msg, DATA data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return CODE_ON_SUCCESS.equals(code);
    }

    public String getMsg() {
        return msg;
    }

    public DATA getData() {
        return data;
    }
}
