package com.tianhe.pay.ui.crm.member;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.crm.member.QueryMember;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class QueryMemberPresenter extends TianHePresenter<QueryMemberContract.View>
        implements QueryMemberContract.Presenter {
    protected Global global;
    UseCase queryTask;

    @Inject
    public QueryMemberPresenter(Global global,
                                @Named("queryMember") UseCase queryTask) {
        this.global = global;
        this.queryTask = queryTask;
    }

    @Override
    public void query(String cardNo) {
        QueryMember query = new QueryMember(global.getShopNo());
        query.setQueryNo(cardNo);
        exeTask(query);
    }

    private void exeTask(QueryMember query) {
        queryTask.setReqParam(query);
        queryTask.execute(new DefaultObserver<Member>() {
            @Override
            public void onNext(Member member) {
                onQuerySuccess(member);
            }

            @Override
            public void onError(Throwable e) {
                onQueryFail(e.getMessage());
            }
        });
    }

    private void onQuerySuccess(Member member) {
        view.onQuerySuccess(member);
    }

    private void onQueryFail(String reason) {
        view.onQueryFail(reason);
    }
}
