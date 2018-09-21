package com.tianhe.pay.data.auth;

import com.google.gson.annotations.SerializedName;

public class QueryAuth {
    @SerializedName("cardNo")
    String auth;

    public QueryAuth() {
    }

    public QueryAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
