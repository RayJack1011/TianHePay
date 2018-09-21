package com.tianhe.pay.ui.cart;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.model.CartManager;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;
import com.tianhe.pay.utils.money.Money;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class ModifyItemPresenter extends TianHePresenter<CartDetailContract.View>
    implements CartDetailContract.Presenter {
    private CartManager cartManager;
    private UseCase calculateTask;

    @Inject
    public ModifyItemPresenter(CartManager cartManager,
                               @Named("calculateModify") UseCase calculateTask) {
        this.cartManager = cartManager;
        this.calculateTask = calculateTask;
    }

    @Override
    public void calculateItem(int index, int quantity, Money price) {
        Order itemOrder = cartManager.forCalculateModifyItem(index, quantity, price);
        calculateTask.setReqParam(itemOrder);
        calculateTask.execute(new DefaultObserver<CalculationResult>(){
            @Override
            public void onNext(@NonNull CalculationResult result) {
                cartManager.onCalculatedModifyItem(result);
                view.calculatedModifyItem(result);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.calculateFail(e.getMessage());
            }
        });
    }
}
