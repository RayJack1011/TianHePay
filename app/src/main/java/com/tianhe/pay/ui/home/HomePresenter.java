package com.tianhe.pay.ui.home;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class HomePresenter extends TianHePresenter<HomeContract.View> implements HomeContract.Presenter {

    private CartManager cartManager;
    private UseCase calculateTask;

    @Inject
    public HomePresenter(CartManager cartManager,
            @Named("calculateKeypad") UseCase calculateTask) {
        this.cartManager = cartManager;
        this.calculateTask = calculateTask;
    }

    @Override
    public void calculateKaypadItem() {
        Order order = cartManager.forCalculateKeypadItem();
        Log.e("qqq","计算折扣----->"+new Gson().toJson(order));
        calculateTask.setReqParam(order);
        calculateTask.execute(new DefaultObserver<CalculationResult>(){
            @Override
            public void onNext(@NonNull CalculationResult result) {
                Log.e("qqq","计算折扣回参（成功）----->"+new Gson().toJson(result));
                cartManager.onCalculatedKeypadItem(result);
                view.calculatedSuccess();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.calculatedFail(e.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
