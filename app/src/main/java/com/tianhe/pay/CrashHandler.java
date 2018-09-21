package com.tianhe.pay;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.tianhe.pay.ui.login.LoginActivity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Map<String, String> infos = new HashMap<String, String>();
    private Class restartActivity;

    public CrashHandler(Context context) {
        this.context = context;
        restartActivity = LoginActivity.class;
    }

    @Override
    public void uncaughtException(Thread t, Throwable error) {
        handleException(error);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore
        }
//        restart();
        // 结束应用
        exitApp();
    }

    private void restart() {
        Intent intent = new Intent(context.getApplicationContext(), restartActivity);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //重启应用，得使用PendingIntent
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(),
                0, intent, FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); // 重启应用
    }

    private void exitApp() {
        ((TianHeApp)context.getApplicationContext()).exitApp();
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        Log.e("Crash", ex.getMessage());
        // 使用 Toast 来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, getTips(ex), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
//        if (mIsDebug) {
//            collectDeviceInfo(context);
//            // 如果用户不赋予外部存储卡的写权限导致的崩溃，会造成循环崩溃
//            saveCrashInfoToFile(ex);
//        }
        return true;
    }

    private String getTips(Throwable error) {
        return "程序异常, 将在3秒回重启!";
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // ignore
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                // ignore
            }
        }
    }

}
