package com.tianhe.pay.ui.auth;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.auth.QueryAuth;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class QueryAuthCardPresenter extends TianHePresenter<QueryAuthContract.View>
    implements QueryAuthContract.Presenter {

    UseCase queryAuth;

    @Inject
    public QueryAuthCardPresenter(@Named("queryAuth") UseCase queryAuth) {
        this.queryAuth = queryAuth;
    }

    @Override
    public void query(String track) {
        queryAuth.setReqParam(new QueryAuth(track));
        queryAuth.execute(new DefaultObserver<Auth>(){
            @Override
            public void onNext(@NonNull Auth auth) {
                view.onQuerySuccess(auth);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.onQueryFail(e.getMessage());
            }
        });
    }
}
