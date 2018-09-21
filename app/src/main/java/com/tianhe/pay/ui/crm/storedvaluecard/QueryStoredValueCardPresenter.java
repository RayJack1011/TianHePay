package com.tianhe.pay.ui.crm.storedvaluecard;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.storedvaluecard.QueryStoredValueCard;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class QueryStoredValueCardPresenter extends TianHePresenter<QueryStoredValueCardContract.View>
        implements QueryStoredValueCardContract.Presenter {
    protected Global global;
    UseCase queryTask;

    @Inject
    public QueryStoredValueCardPresenter(Global global,
                                         @Named("queryStoredValue") UseCase queryTask) {
        this.global = global;
        this.queryTask = queryTask;
    }

    @Override
    public void query(String track, String password) {
        String[] cardInfo = track.split("=");
        QueryStoredValueCard query = new QueryStoredValueCard(global.getShopNo());
        query.setCardNo(cardInfo[0]);
        query.setCardValidCode(cardInfo[1]);
        query.setPassword(password);
        exeTask(query);
    }

    private void exeTask(QueryStoredValueCard query) {
        queryTask.setReqParam(query);
        queryTask.execute(new DefaultObserver<StoredValueCard>() {
            @Override
            public void onNext(StoredValueCard card) {
                onQuerySuccess(card);
            }

            @Override
            public void onError(Throwable e) {
                onQueryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(StoredValueCard card) {
        view.onQuerySuccess(card);
    }

    private void onQueryFail(String reason) {
        view.onQueryFail(reason);
    }
}
