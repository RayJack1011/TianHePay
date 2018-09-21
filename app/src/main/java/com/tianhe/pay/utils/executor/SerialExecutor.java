package com.tianhe.pay.utils.executor;

import java.util.concurrent.Executor;

public interface SerialExecutor extends Executor {
    void cancel(Runnable runnable);

    boolean executeDelayed(Runnable runnable, long delayMillis);

    void post(Runnable runnable);
}
