package com.tianhe.pay.data.crm.storedvaluecard;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.crm.CrmApi;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryStoredValueCardUseCase extends UseCase<QueryStoredValueCard> {

    CrmApi crmApi;
    QueryStoredValueCard query;

    @Inject
    public QueryStoredValueCardUseCase(@MainThread Scheduler mainScheduler,
                                       @RpcThread Scheduler rpcScheduler,
                                       CrmApi crmApi) {
        super(mainScheduler, rpcScheduler);
        this.crmApi = crmApi;
    }

    @Override
    protected Observable<StoredValueCard> buildUseCaseObservable() {
        return crmApi.queryStoredValueCard(query);
    }

    @Override
    public void setReqParam(QueryStoredValueCard reqParam) {
        this.query = reqParam;
    }
}
