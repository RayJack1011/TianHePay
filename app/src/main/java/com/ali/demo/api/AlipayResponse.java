package com.ali.demo.api;

import com.ali.demo.api.internal.mapping.ApiField;
import com.ali.demo.api.internal.utils.StringUtils;

import java.util.Map;

public abstract class AlipayResponse {
    @ApiField("code")
    private String code;
    @ApiField("msg")
    private String msg;
    @ApiField("sub_code")
    private String subCode;
    @ApiField("sub_msg")
    private String subMsg;
    private String body;
    private Map<String, String> params;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return this.subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isSuccess() {
        return StringUtils.isEmpty(this.subCode);
    }
}
