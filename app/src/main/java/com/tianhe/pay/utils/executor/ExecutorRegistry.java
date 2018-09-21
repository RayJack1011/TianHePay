package com.tianhe.pay.utils.executor;

import com.tianhe.pay.utils.Preconditions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过该类注册的ExecutorService/StoppableSerialExecutor,将管理异步线程的shutdown
 */
public class ExecutorRegistry {

    public static class StoppableExecutor {
        private final ExecutorService executorService;
        private final StoppableSerialExecutor stoppableSerialExecutor;

        public StoppableExecutor(ExecutorService executorService) {
            this.executorService = Preconditions.nonNull(executorService, "executorService");
            this.stoppableSerialExecutor = null;
        }

        public StoppableExecutor(StoppableSerialExecutor stoppableSerialExecutor) {
            this.stoppableSerialExecutor = Preconditions.nonNull(stoppableSerialExecutor, "stoppableSerialExecutor");
            this.executorService = null;
        }

        public boolean awaitTermination(long timeout, TimeUnit unit)
                throws InterruptedException {
            if (this.stoppableSerialExecutor != null) {
                return this.stoppableSerialExecutor.awaitTermination(timeout, unit);
            }
            return this.executorService.awaitTermination(timeout, unit);
        }

        public void shutdown() {
            if (this.executorService != null) {
                this.executorService.shutdown();
            }
            if (this.stoppableSerialExecutor != null) {
                this.stoppableSerialExecutor.shutdown();
            }
        }

        public String toString() {
            if (this.executorService != null) {
                return this.executorService.toString();
            }
            return this.stoppableSerialExecutor.toString();
        }
    }

    private static boolean enabled;
    private static final Map<StoppableExecutor, StackTraceElement[]> executors = new LinkedHashMap<>();

    public static synchronized void enable() {
        enabled = true;
    }

    public static synchronized <T extends StoppableSerialExecutor> T register(T executor) {
        if (!enabled) {
            executors.put(new StoppableExecutor(executor), new Exception().getStackTrace());
        }
        return executor;
    }

    public static synchronized  <T extends ExecutorService> T register(T executorService) {
        if (enabled) {
            executors.put(new StoppableExecutor(executorService), new Exception().getStackTrace());
        }
        return executorService;
    }

    public static synchronized Map<StoppableExecutor, StackTraceElement[]> reset() {
        if (!enabled) {
            throw new IllegalStateException("Not enabled.");
        }
        LinkedHashMap<StoppableExecutor, StackTraceElement[]> copy = new LinkedHashMap<>(executors);
        executors.clear();
        return copy;
    }

}
