package com.tianhe.pay.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AndroidLeaks {
    private static final int HANDLER_THREAD_LEAK_CLEANING_MS = 1000;
    private static final Executor backgroundExecutor = Executors.newCachedThreadPool(Threads.backgroundThreadFactory
            ("android-leaks"));

    private static void fixMediaSessionLegacyHelperLeak(final Application app) {
        if (Build.VERSION.SDK_INT != 21) {
            return;
        }
        backgroundExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Class.forName("android.media.session.MediaSessionLegacyHelper")
                            .getDeclaredMethod("getHelper", Context.class)
                            .invoke(null, app);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                } catch (ClassNotFoundException e) {
                }
            }
        });
    }

    private static void fixTextLinePoolLeak(final Application app) {
        backgroundExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Field sCachedField = Class.forName("android.text.TextLine").getDeclaredField("sCached");
                    sCachedField.setAccessible(true);
                    final Object sCached = sCachedField.get(null);
                    if (sCached != null && sCached.getClass().isArray()) {
                        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter(){
                            @Override
                            public void onActivityDestroyed(Activity activity) {
                                // Clear the array.
                                for (int i = 0, size = Array.getLength(sCached); i < size; i ++) {
                                    Array.set(sCached, i, null);
                                }
                            }
                        });
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    public static void flushStackLocalLeaks(Looper looper) {
        Handler handler = new Handler(looper) {
            public void handleMessage(Message message) {
                sendMessageDelayed(obtainMessage(), 1000L);
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 1000L);
    }

    public static void plugLeaks(Application app) {
        fixMediaSessionLegacyHelperLeak(app);
        fixTextLinePoolLeak(app);
        IMMLeaks.fixFocusedViewLeak(app);
    }
}
