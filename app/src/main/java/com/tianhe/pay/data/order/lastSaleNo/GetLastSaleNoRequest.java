package com.tianhe.pay.data.order.lastSaleNo;

public class GetLastSaleNoRequest {
    String terminalId;
    long date;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
