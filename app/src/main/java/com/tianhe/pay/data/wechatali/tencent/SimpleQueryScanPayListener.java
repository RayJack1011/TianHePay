package com.tianhe.pay.data.wechatali.tencent;

import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;

public interface SimpleQueryScanPayListener {

    void onFail();

    void onSuccess(ScanPayQueryResData data);
}
