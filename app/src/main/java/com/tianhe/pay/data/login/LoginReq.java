package com.tianhe.pay.data.login;

public class LoginReq {
    public String userNo;
    public String passwd;
    public String mac;

    public LoginReq(String userNo, String passwd, String mac) {
        this.userNo = userNo;
        this.passwd = passwd;
        this.mac = mac;
    }

    public String getUserNo() {
        return userNo;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getMac() {
        return mac;
    }
}
