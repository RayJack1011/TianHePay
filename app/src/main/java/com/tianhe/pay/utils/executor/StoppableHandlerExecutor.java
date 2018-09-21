package com.tianhe.pay.utils.executor;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.tianhe.pay.utils.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StoppableHandlerExecutor implements StoppableSerialExecutor {
    private final class RemovingRunnableWrapper implements Runnable {
        private final Runnable wrapped;

        public RemovingRunnableWrapper(Runnable wrapped) {
            this.wrapped = wrapped;
        }

        public Runnable getWrapped() {
            return wrapped;
        }

        @Override
        public void run() {
            synchronized (lock) {
                if (isShutdown) {
                    return;
                }
                queuedRunnables.remove(this);
                runningCounter.incrementAndGet();
            }
            try {
                wrapped.run();
            } finally {
                synchronized (lock) {
                    if (runningCounter.decrementAndGet() == 0) {
                        terminationLatch.countDown();
                    }
                }
            }
        }
    }

    private final Handler handler;
    private volatile boolean isShutdown;
    private final Object lock = new Object();
    private final List<RemovingRunnableWrapper> queuedRunnables = new ArrayList<>();
    private final boolean quitLooperOnShutdown;
    private final AtomicInteger runningCounter = new AtomicInteger(1);
    private final CountDownLatch terminationLatch = new CountDownLatch(1);


    public StoppableHandlerExecutor(Handler handler, boolean quitLooperOnShutdown) {
        this.handler = handler;
        this.quitLooperOnShutdown = quitLooperOnShutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.checkState(isShutdown, "Not shutdown.");
        return terminationLatch.await(timeout, timeUnit);
    }

    @Override
    public void cancel(Runnable wrapped) {
        synchronized (lock) {
            Iterator<RemovingRunnableWrapper> iterator = queuedRunnables.iterator();
            while (iterator.hasNext()) {
                RemovingRunnableWrapper wrapper = iterator.next();
                if (wrapper.getWrapped().equals(wrapped)) {
                    iterator.remove();
                    handler.removeCallbacks(wrapper);
                }
            }
        }
    }

    @Override
    public boolean isShutDown() {
        return isShutdown;
    }

    @Override
    public void shutdown() {
        synchronized (lock) {
            if (isShutdown) {
                return;
            }
            isShutdown = true;
            if (runningCounter.decrementAndGet() == 0) {
                terminationLatch.countDown();
            }
            Iterator<RemovingRunnableWrapper> iterator = queuedRunnables.iterator();
            while (iterator.hasNext()) {
                RemovingRunnableWrapper wrapper = iterator.next();
                this.handler.removeCallbacks(wrapper);
            }
        }
        queuedRunnables.clear();
        if (quitLooperOnShutdown) {
            handler.getLooper().quit();
        }
    }

    @Override
    public void post(Runnable runnable) {
        synchronized (lock) {
            if ((!isShutdown) && (!handler.post(removeOnRun(runnable)))) {
                throw new IllegalStateException("Could not post.");
            }
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (lock) {
            if (isShutdown) {
                return;
            }
            if (handler.getLooper().getThread() != Thread.currentThread()) {
                handler.post(removeOnRun(runnable));
                return;
            }
        }
        runnable.run();
    }

    @Override
    public boolean executeDelayed(Runnable runnable, long delayMillis) {
        synchronized (lock) {
            if (isShutdown) {
                return false;
            }
            return handler.postDelayed(removeOnRun(runnable), delayMillis);

        }
    }

    private RemovingRunnableWrapper removeOnRun(Runnable r) {
        RemovingRunnableWrapper wrapper = new RemovingRunnableWrapper(r);
        queuedRunnables.add(wrapper);
        return wrapper;
    }
}
