package com.tianhe.pay.ui.crm.coupon;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.crm.coupon.QueryCoupon;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class QueryCouponPresenter extends TianHePresenter<QueryCouponContract.View>
        implements QueryCouponContract.Presenter {
    protected Global global;
    UseCase queryTask;

    @Inject
    public QueryCouponPresenter(Global global,
                                @Named("queryCoupon") UseCase queryTask) {
        this.global = global;
        this.queryTask = queryTask;
    }

    @Override
    public void query(String couponNo) {
        QueryCoupon query = new QueryCoupon(global.getShopNo());
        query.setCouponNo(couponNo);
        exeTask(query);
    }

    private void exeTask(QueryCoupon query) {
        if(CommomData.isCouponReturn){
            query.setRefund();
        }
//        Log.e("qqq","退券入参：-----》"+new Gson().toJson(query));
        queryTask.setReqParam(query);
        queryTask.execute(new DefaultObserver<Coupon>() {
            @Override
            public void onNext(Coupon coupon) {
                onQuerySuccess(coupon);
            }

            @Override
            public void onError(Throwable e) {
                onQueryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(Coupon coupon) {
        view.onQuerySuccess(coupon);
    }

    private void onQueryFail(String reason) {
        view.onQueryFail(reason);
    }
}
