package com.tianhe.pay.utils;

import android.os.Looper;

import com.tianhe.pay.utils.executor.Executors;
import com.tianhe.pay.utils.executor.SerialExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.schedulers.ScheduledRunnable;
import io.reactivex.plugins.RxJavaPlugins;

public class RxSchedulers {
    public static Scheduler mainThread() {
        return new Scheduler() {
            private final Looper mainLooper = Looper.getMainLooper();
            private final SerialExecutor mainThreadExecutor = Executors.stoppableMainThreadExecutor();

            @Override
            public Worker createWorker() {
                return new Worker() {
                    CompositeDisposable scheduledWork = new CompositeDisposable();

                    @Override
                    public Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                        Runnable wrapRun = RxJavaPlugins.onSchedule(run);
                        if (Looper.myLooper() == mainLooper) {
                            wrapRun.run();
                            return Disposables.empty();
                        }
                        if ((delay == 0L) && (Looper.myLooper() == mainLooper)) {
                            run.run();
                            return Disposables.empty();
                        } else {
                            final ScheduledRunnable scheduled = new ScheduledRunnable(wrapRun, scheduledWork);
                            scheduled.setFuture(new Future<Void>() {
                                @Override
                                public boolean cancel(boolean mayInterruptIfRunning) {
                                    mainThreadExecutor.cancel(scheduled);
                                    return true;
                                }
                                @Override
                                public boolean isCancelled() {
                                    return false;
                                }
                                @Override
                                public boolean isDone() {
                                    return false;
                                }
                                @Override
                                public Void get() throws InterruptedException, ExecutionException {
                                    return null;
                                }
                                @Override
                                public Void get(long timeout, @android.support.annotation.NonNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                                    return null;
                                }
                            });
                            scheduledWork.add(scheduled);
                            mainThreadExecutor.executeDelayed(scheduled, unit.toMillis(delay));
                            return scheduled;
                        }
                    }

                    @Override
                    public void dispose() {
                        scheduledWork.dispose();
                    }

                    @Override
                    public boolean isDisposed() {
                        return scheduledWork.isDisposed();
                    }
                };
            }
        };
    }


}
