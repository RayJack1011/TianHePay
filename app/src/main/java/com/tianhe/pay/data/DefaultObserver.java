package com.tianhe.pay.data;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class DefaultObserver<D> extends DisposableObserver<D> {

    @Override
    public void onNext(@NonNull D d) {
    }

    @Override
    public void onError(@NonNull Throwable e) {
        dispose();
    }

    @Override
    public void onComplete() {
        dispose();
    }
}
