package com.tianhe.pay.utils.executor;

import java.util.concurrent.TimeUnit;

public interface StoppableSerialExecutor extends SerialExecutor {
    boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException;

    boolean isShutDown();

    void shutdown();
}
