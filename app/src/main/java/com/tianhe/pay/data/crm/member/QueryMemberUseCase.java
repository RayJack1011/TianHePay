package com.tianhe.pay.data.crm.member;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.CrmApi;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryMemberUseCase extends UseCase<QueryMember> {

    CrmApi crmApi;
    QueryMember query;

    @Inject
    public QueryMemberUseCase(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              CrmApi crmApi) {
        super(mainScheduler, rpcScheduler);
        this.crmApi = crmApi;
    }

    @Override
    protected Observable<Member> buildUseCaseObservable() {
        return crmApi.queryMember(query);
    }

    @Override
    public void setReqParam(QueryMember reqParam) {
        this.query = reqParam;
    }
}
