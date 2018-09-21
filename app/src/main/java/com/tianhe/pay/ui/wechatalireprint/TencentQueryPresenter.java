package com.tianhe.pay.ui.wechatalireprint;

import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;
import javax.inject.Named;

public class TencentQueryPresenter extends TianHePresenter<WechatAliQueryContract.View>
    implements WechatAliQueryContract.Presenter {
    Global global;
    UseCase tencentQueryTask;

    @Inject
    public TencentQueryPresenter(Global global,
                                 @Named("tencentQuery") UseCase queryTask) {
        this.tencentQueryTask = queryTask;
        this.global = global;
    }

    @Override
    public void queryWechatAli(String relNo) {
        // 原生微信支付接口扫码到的条码是商户交易单号
        ScanPayQueryReqData request = new ScanPayQueryReqData("", relNo);
        tencentQueryTask.setReqParam(request);
        tencentQueryTask.execute(new DefaultObserver<ScanPayQueryResData>() {
            @Override
            public void onNext(ScanPayQueryResData response) {
                onQuerySuccess(response);
            }

            @Override
            public void onError(Throwable e) {
                view.queryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(ScanPayQueryResData response) {
        if (!response.isSuccess()) {
            view.queryFail(response.getErr_code_des());
            return;
        }
        WechatAliOrder order = new WechatAliOrder();
        order.setReprint(true);
        order.setLowOrderId(response.getOut_trade_no());
        order.setUpOrderId(response.getTransaction_id());
        order.setShopNo(global.getShopNo());
        order.setUserNo(global.getUserNo());
        order.setDatetime(response.getTime_end());
        order.setOrderState(response.stateName());
        order.setPayMoney(Money.createAsFen(response.getTotal_fee()));
        view.querySuccess(order);
    }

}
