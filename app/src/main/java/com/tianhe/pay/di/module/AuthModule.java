package com.tianhe.pay.di.module;

import com.tianhe.pay.data.auth.QueryAuthUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.auth.QueryAuthCardPresenter;
import com.tianhe.pay.ui.auth.QueryAuthContract;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AuthModule {

    @PerFragment
    @Binds
    abstract QueryAuthContract.Presenter queryAuthCardPresenter(QueryAuthCardPresenter presenter);


    @Provides
    @PerFragment
    @Named("queryAuth")
    static UseCase providerWechatAliQuery(QueryAuthUseCase task) {
        return task;
    }

}
