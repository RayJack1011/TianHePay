package com.tianhe.pay.utils.executor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.tianhe.pay.utils.AndroidLeaks;
import com.tianhe.pay.utils.Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executors {
    private static final long DEFAULT_CACHE_TIMEOUT_SECONDS = 60L;
    private static final int DEFAULT_MAX_THREAD_COUNT = 5;

    public static AndroidMainThread androidMainThread() {
        return ExecutorRegistry.register(new AndroidMainThread());
    }

    public static ThreadPoolExecutor newBoundedCachedThreadPool(int nThreads, long cacheTimeout,
                                                                TimeUnit cacheTimeoutUnit, ThreadFactory threadFactory) {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads,
                cacheTimeout, cacheTimeoutUnit, queue, threadFactory);
        pool.allowCoreThreadTimeOut(true);
        return ExecutorRegistry.register(pool);
    }

    public static ThreadPoolExecutor newBoundedCachedThreadPool(ThreadFactory threadFactory) {
        return newBoundedCachedThreadPool(DEFAULT_MAX_THREAD_COUNT, DEFAULT_CACHE_TIMEOUT_SECONDS, TimeUnit.SECONDS, threadFactory);
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return ExecutorRegistry.register(java.util.concurrent.Executors.newCachedThreadPool(threadFactory));
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return ExecutorRegistry.register(java.util.concurrent.Executors.newFixedThreadPool(nThreads, threadFactory));
    }

    public static ExecutorService newSingleThreadExecutor(String threadName) {
        return ExecutorRegistry.register(java.util.concurrent.Executors.newSingleThreadExecutor(Threads.namedThreadFactory(threadName)));
    }

    public static StoppableHandlerExecutor stoppableHandlerExecutor(Handler handler, boolean quitLooperOnShutdown) {
        return ExecutorRegistry.register(new StoppableHandlerExecutor(handler, quitLooperOnShutdown));
    }

    public static StoppableHandlerExecutor stoppableMainThreadExecutor() {
        return stoppableHandlerExecutor(new Handler(Looper.getMainLooper()), false);
    }

    public static StoppableSerialExecutor stoppableNamedThreadExecutor(String threadName, int priority, boolean daemon) {
        HandlerThread handlerThread = new HandlerThread(threadName, priority);
        handlerThread.setDaemon(daemon);
        handlerThread.start();
        AndroidLeaks.flushStackLocalLeaks(handlerThread.getLooper());
        return stoppableHandlerExecutor(new Handler(handlerThread.getLooper()), true);
    }
}
