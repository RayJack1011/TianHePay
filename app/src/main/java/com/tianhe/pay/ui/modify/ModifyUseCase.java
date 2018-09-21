//package com.tianhe.pay.ui.modify;
//
//import com.tianhe.pay.data.MainThread;
//import com.tianhe.pay.data.Repository;
//import com.tianhe.pay.data.RpcThread;
//import com.tianhe.pay.data.UseCase;
//import com.tianhe.pay.db.DbCache;
//
//import java.util.Date;
//
//import javax.inject.Inject;
//
//import io.reactivex.Observable;
//import io.reactivex.Scheduler;
//import io.reactivex.functions.Consumer;
//
///**
// * Created by wangya3 on 2018/3/27.
// */
//
//public class ModifyUseCase extends UseCase<ModifyReq> {
//    private transient Repository repository;
//    private DbCache dbCache;
//    private ModifyReq req;
//
//    @Inject
//    public ModifyUseCase(@MainThread Scheduler mainScheduler,
//                         @RpcThread Scheduler rpcScheduler,
//                         Repository repository, DbCache dbCache) {
//        super(mainScheduler, rpcScheduler);
//        this.repository = repository;
//        this.dbCache = dbCache;
//    }
//
//    @Override
//    public void setReqParam(ModifyReq req) {
//        this.req = req;
//    }
//
//    @Override
//    protected Observable<ModifyResp> buildUseCaseObservable() {
//        return repository.modify(req)
//                .doOnNext(new Consumer<ModifyResp>() {
//                    @Override
//                    public void accept(ModifyResp modifyResp) throws Exception {
//                        dbCache.reduceOrders(new Date());
//                    }
//                });
//    }
//}
