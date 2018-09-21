package com.tianhe.pay.di.module;

import com.tianhe.pay.data.login.UserLogin;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.ui.login.LoginContract;
import com.tianhe.pay.ui.login.LoginPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract LoginFragment loginFragment();

    @PerFragment
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);

    @Provides
    @PerFragment
    @Named("login")
    static UseCase providerLoginTask(UserLogin loginTask) {
        return loginTask;
    }

}
