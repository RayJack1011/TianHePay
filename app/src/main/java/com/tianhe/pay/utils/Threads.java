package com.tianhe.pay.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Threads {
    public static final String FILE_THREAD_NAME = "TianHeFileIO";

    public static ThreadFactory backgroundThreadFactory(final String threadName) {
        return new ThreadFactory() {
            private final AtomicInteger threadCounter = new AtomicInteger(0);

            @Override
            public Thread newThread(@NonNull final Runnable r) {
                return new Thread(new Runnable() {
                    public void run() {
                        try {
                            android.os.Process.setThreadPriority(10);
                            r.run();
                        } catch (UnsatisfiedLinkError e) {
                            throw new RuntimeException(
                                    "UnsatisfiedLinkError caught when trying to set background thread priority.", e);
                        }
                    }
                }, threadName + "-" + threadCounter.getAndIncrement());
            }

        };
    }

    public static ThreadFactory namedThreadFactory(final String threadName) {
        return new ThreadFactory() {
            private final AtomicInteger threadCounter = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, threadName + "-" + threadCounter.getAndIncrement());
            }
        };
    }
}
