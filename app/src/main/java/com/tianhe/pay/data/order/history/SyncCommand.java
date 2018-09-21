package com.tianhe.pay.data.order.history;

/**
 * 同步数据的参数
 */
public class SyncCommand {
    long time;
    String mac;
    String isPractice;

    public SyncCommand() {
    }

    public SyncCommand(long time, String mac) {
        this.time = time;
        this.mac = mac;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIsPractice() {
        return isPractice;
    }

    public void setIsPractice(String isPractice) {
        this.isPractice = isPractice;
    }
}
