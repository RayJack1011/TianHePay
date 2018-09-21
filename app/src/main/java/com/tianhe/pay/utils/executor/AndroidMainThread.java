package com.tianhe.pay.utils.executor;

import android.os.Handler;
import android.os.Looper;

import com.tianhe.pay.utils.executor.StoppableHandlerExecutor;

public class AndroidMainThread extends StoppableHandlerExecutor implements MainThread {
    public AndroidMainThread() {
        super(new Handler(Looper.getMainLooper()), false);
    }
}
