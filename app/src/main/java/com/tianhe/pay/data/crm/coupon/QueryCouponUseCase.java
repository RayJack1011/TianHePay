package com.tianhe.pay.data.crm.coupon;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.CrmApi;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class QueryCouponUseCase extends UseCase<QueryCoupon> {
    CrmApi crmApi;
    QueryCoupon query;

    @Inject
    public QueryCouponUseCase(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              CrmApi crmApi) {
        super(mainScheduler, rpcScheduler);
        this.crmApi = crmApi;
    }

    @Override
    protected Observable<Coupon> buildUseCaseObservable() {
        return crmApi.queryCoupon(query);
    }

    @Override
    public void setReqParam(QueryCoupon reqParam) {
        this.query = reqParam;
    }
}
