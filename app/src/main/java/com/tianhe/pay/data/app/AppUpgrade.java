package com.tianhe.pay.data.app;

public class AppUpgrade {
    private String remark;      // 更新信息
    private String url;         // 下载地址
    private String version;     // 最新版本号

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
