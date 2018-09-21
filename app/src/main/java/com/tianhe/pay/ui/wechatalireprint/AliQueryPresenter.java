package com.tianhe.pay.ui.wechatalireprint;

import android.util.Log;

import com.ali.AlipayTradeStates;
import com.ali.demo.api.response.AlipayTradeQueryResponse;
import com.ali.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.ali.demo.trade.model.result.AlipayF2FQueryResult;
import com.google.gson.Gson;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;
import javax.inject.Named;

public class AliQueryPresenter extends TianHePresenter<WechatAliQueryContract.View>
    implements WechatAliQueryContract.Presenter {
    Global global;
    UseCase aliQueryTask;

    @Inject
    public AliQueryPresenter(Global global,
                             @Named("aliQuery") UseCase queryTask) {
        this.aliQueryTask = queryTask;
        this.global = global;
    }

    @Override
    public void queryWechatAli(String relNo) {
        AlipayTradeQueryRequestBuilder builder =
                new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(relNo);
        Log.e("qqq","支付宝查询入参----->"+new Gson().toJson(builder));
        aliQueryTask.setReqParam(builder);
        aliQueryTask.execute(new DefaultObserver<AlipayF2FQueryResult>() {
            @Override
            public void onNext(AlipayF2FQueryResult result) {
                onQuerySuccess(result);
            }

            @Override
            public void onError(Throwable e) {
                view.queryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(AlipayF2FQueryResult result) {
        AlipayTradeQueryResponse response = result.getResponse();
        if (!result.isTradeSuccess()) {
            if (response != null) {
                view.queryFail(response.getSubMsg());
            } else {
                view.queryFail("查询失败!");
            }
            return;
        }
        WechatAliOrder order = new WechatAliOrder();
        order.setReprint(true);
        order.setLowOrderId(response.getOutTradeNo());
        order.setUpOrderId(response.getTradeNo());
        order.setShopNo(global.getShopNo());
        order.setUserNo(global.getUserNo());
        order.setDatetime(Times.formatDefault(response.getSendPayDate()));
        order.setOrderState(AlipayTradeStates.stateName(response.getTradeStatus()));
        order.setPayMoney(Money.createAsYuan(response.getTotalAmount()));
        view.querySuccess(order);
    }

}
