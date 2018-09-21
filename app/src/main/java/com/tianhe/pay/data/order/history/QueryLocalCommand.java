package com.tianhe.pay.data.order.history;

import java.util.Date;

public class QueryLocalCommand {
    Date date;
    String isPractice;

    public QueryLocalCommand() {
    }

    public QueryLocalCommand(Date date, String isPractice) {
        this.date = date;
        this.isPractice = isPractice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIsPractice() {
        return isPractice;
    }

    public void setIsPractice(String isPractice) {
        this.isPractice = isPractice;
    }
}
