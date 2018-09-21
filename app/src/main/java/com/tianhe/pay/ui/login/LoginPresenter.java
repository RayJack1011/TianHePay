package com.tianhe.pay.ui.login;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.login.LoginReq;
import com.tianhe.pay.data.login.LoginResp;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;

public class LoginPresenter extends TianHePresenter<LoginContract.View> implements LoginContract.Presenter {

    private UseCase loginTask;
    private String mac;
    Global global;

    @Inject
    public LoginPresenter(@Named("login") UseCase loginTask,
                          @Named("wifiMac") String mac,
                          Global global) {
        this.loginTask = loginTask;
        this.mac = mac;
        this.global = global;
    }

    @Override
    public void login() {
        loginTask.setReqParam(getLoginRequest());
        loginTask.execute(new DefaultObserver<LoginResp>() {
            @Override
            public void onNext(LoginResp loginResp) {
                loginSuccess(loginResp);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                loginFail(e);
            }
        });
    }

    private LoginReq getLoginRequest() {
        Log.e("qqq",mac+"：：：：mac");
        return new LoginReq(view.username().toString(),
                view.password().toString(), mac);
    }

    @Override
    public void onDestroy() {
        loginTask.cancel();
        loginTask = null;
        view = null;
        super.onDestroy();
    }

    private void loginSuccess(LoginResp loginResp) {
        global.onLoginSuccess(getLoginRequest().userNo, loginResp);
        view.loginSuccess();
    }

    private void loginFail(Throwable e) {
        view.loginFailed(e.getMessage());
    }

}
