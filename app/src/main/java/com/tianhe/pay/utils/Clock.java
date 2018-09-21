package com.tianhe.pay.utils;

import android.location.Location;
import android.os.Build;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.GregorianCalendar;

public interface Clock {
    Clock DEFAULT = new Clock() {
        @Override
        public long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }

        @Override
        public long getElapsedRealTime() {
            return SystemClock.elapsedRealtime();
        }

        @Override
        public long getUptimeMillis() {
            return SystemClock.uptimeMillis();
        }

        @Override
        public Calendar gregorianCalender() {
            return GregorianCalendar.getInstance();
        }

        @Override
        public boolean withinPast(Location location, long millisAgo) {
            long deltaMs;
            if (Build.VERSION.SDK_INT >= 17) {
                deltaMs = SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos();
            } else {
                deltaMs = getCurrentTimeMillis() - location.getTime();
            }
            return deltaMs > millisAgo;
        }
    };

    long getCurrentTimeMillis();

    long getElapsedRealTime();

    long getUptimeMillis();

    boolean withinPast(Location location, long millisAgo);

    Calendar gregorianCalender();
}
