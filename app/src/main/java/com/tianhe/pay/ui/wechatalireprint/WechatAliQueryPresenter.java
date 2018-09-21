package com.tianhe.pay.ui.wechatalireprint;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.data.wechatali.query.TongguanQueryRequest;
import com.tianhe.pay.data.wechatali.query.TongguanQueryResponse;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class WechatAliQueryPresenter extends TianHePresenter<WechatAliQueryContract.View>
    implements WechatAliQueryContract.Presenter {
    Global global;
    UseCase wechatAliQueryTask;

    @Inject
    public WechatAliQueryPresenter(Global global,
            @Named("wechatAliQuery") UseCase queryTask) {
        this.wechatAliQueryTask = queryTask;
        this.global = global;
    }

    @Override
    public void queryWechatAli(final String relNo) {
        TongguanQueryRequest request = new TongguanQueryRequest();
        request.setAccount(global.getWechatAliAccount());
        request.setKey(global.getWechatAliKey());
//        request.setUpOrderId(relNo);
        request.setLowOrderId(relNo);
        wechatAliQueryTask.setReqParam(request);
        wechatAliQueryTask.execute(new DefaultObserver<TongguanQueryResponse>() {
            @Override
            public void onNext(TongguanQueryResponse response) {
                onQuerySuccess(response);
            }

            @Override
            public void onError(Throwable e) {
                view.queryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(TongguanQueryResponse response) {
        if (!response.isSuccess()) {
            view.queryFail(response.getMessage());
            return;
        }
        WechatAliOrder order = new WechatAliOrder();
        order.setReprint(true);
        order.setLowOrderId(response.getLowOrderId());
        order.setUpOrderId(response.getUpOrderId());
        order.setShopNo(global.getShopNo());
        order.setPayName(CommomData.signName);
        order.setUserNo(global.getUserNo());
        order.setDatetime(response.getPayTime());
        order.setOrderState(response.stateName());
        order.setPayMoney(response.getPayMoney());
        view.querySuccess(order);
    }

}
