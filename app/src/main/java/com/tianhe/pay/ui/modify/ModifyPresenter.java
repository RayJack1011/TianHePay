//package com.tianhe.pay.ui.modify;
//
//
//import com.tianhe.pay.CommomData;
//import com.tianhe.pay.data.DefaultObserver;
//import com.tianhe.pay.data.UseCase;
//import com.tianhe.pay.model.Global;
//import com.tianhe.pay.ui.TianHePresenter;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import io.reactivex.annotations.NonNull;
//
///**
// * Created by wangya3 on 2018/3/27.
// */
//
//public class ModifyPresenter extends TianHePresenter<ModifyContract.View> implements ModifyContract.Presenter {
//
//    private UseCase loginTask;
//    private String shopNo;
//    Global global;
//
//    @Inject
//    public ModifyPresenter(@Named("modify") UseCase loginTask,
//                          @Named("shopNo") String shopNo,
//                          Global global) {
//        this.loginTask = loginTask;
//        this.shopNo = shopNo;
//        this.global = global;
//    }
//
//
//
//    @Override
//    public void modify() {
//        loginTask.setReqParam(getModifyRequest());
//        loginTask.execute(new DefaultObserver<ModifyResp>() {
//            @Override
//            public void onNext(ModifyResp modifyResp) {
//                loginSuccess(modifyResp);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                loginFail(e);
//            }
//        });
//    }
//
////    private ModifyReq getModifyRequest() {
////        return new ModifyReq(view.username().toString(),
////                view.oldPassword().toString(),view.newPassword().toString(), CommomData.shopName);
////    }
//
//    private void loginSuccess(ModifyResp modifyResp) {
////        global.onLoginSuccess(getLoginRequest().userNo, loginResp);
//        view.modifySuccess();
//    }
//
//    private void loginFail(Throwable e) {
//        view.modifyFailed(e.getMessage());
//    }
//
//}
