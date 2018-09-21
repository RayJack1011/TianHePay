package com.tianhe.pay.data;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class UseCase<R> {
    private Scheduler mainScheduler;
    private Scheduler rpcScheduler;

    private Disposable disposable;

    public UseCase(Scheduler mainScheduler, Scheduler rpcScheduler) {
        this.mainScheduler = mainScheduler;
        this.rpcScheduler = rpcScheduler;
    }

    protected abstract Observable buildUseCaseObservable();

    public abstract void setReqParam(R reqParam);

    public void execute(DisposableObserver observer) {
        disposable = (Disposable) buildUseCaseObservable()
                .subscribeOn(rpcScheduler)
                .observeOn(mainScheduler)
                .subscribeWith(observer);
    }

    public void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
