package com.tianhe.pay.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.AM_PM;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;


public class Times {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIANHE_CRM_TIME = "yyyyMMddHHmmssSSS";
    private static final String yyMMdd = "yyMMdd";
    private static final String yyMMddHHmmss = "yyMMddHHmmss";
    private static final String yyyyMMdd = "yyyyMMdd";
    private static final String yyyy_MM_dd = "yyyy-MM-dd";
    private static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    static final ThreadLocal<DateFormat> DEFAULT_DATE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_FORMAT, Locale.CHINA);
        }
    };
    static final ThreadLocal<DateFormat> CRM_TIME_FORMAT = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat(TIANHE_CRM_TIME, Locale.CHINA);
        }
    };

    private static final ThreadLocal<SimpleDateFormat> yyMMddLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(yyMMdd, Locale.getDefault());
        }
    };

    private static final ThreadLocal<SimpleDateFormat> yyyyMMddLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(yyyyMMdd, Locale.getDefault());
        }
    };

    private static final ThreadLocal<SimpleDateFormat> yyMMddHHmmssLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(yyMMddHHmmss, Locale.getDefault());
        }
    };

    private static final ThreadLocal<SimpleDateFormat> yyyy_MM_ddLocal = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(yyyy_MM_dd, Locale.getDefault());
        }
    };
    private static final ThreadLocal<SimpleDateFormat> yyyyMMddHHmmssLocal = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(yyyyMMddHHmmss, Locale.getDefault());
        }
    };

    private static final ThreadLocal<Calendar> calendarLocal = new ThreadLocal<Calendar>() {
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    };

    public static String nowDate() {
        return DEFAULT_DATE_TIME_FORMAT.get().format(new Date());
    }

    public static String nowDateForCrm() {
        return CRM_TIME_FORMAT.get().format(new Date());
    }

    public static String yyMMdd(Date date) {
        return yyMMddLocal.get().format(date);
    }

    public static String formatDefault(Date date) {
        return DEFAULT_DATE_TIME_FORMAT.get().format(date);
    }

    public static String yyyyMMdd(Date date) {
        return yyyyMMddLocal.get().format(date);
    }

    public static String yyyy_MM_dd(Date date) {
        return yyyy_MM_ddLocal.get().format(date);
    }

    public static String yyyyMMddHHmmss(Date date) {
        return yyyyMMddHHmmssLocal.get().format(date);
    }

    public static Date yyyy_MM_ddParse(String dateStr) {
        try {
            return yyyy_MM_ddLocal.get().parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String yyMMddHHmmss(Date date) {
        return yyMMddHHmmssLocal.get().format(date);
    }

    public static boolean onDifferentDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(YEAR) != calendar2.get(YEAR)
                || calendar1.get(DAY_OF_YEAR) != calendar2.get(DAY_OF_YEAR);
    }

    public static Date asDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(YEAR, year);
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    public static long countDaysBetween(Date from, Date to) {
        Preconditions.nonNull(from, "from");
        Preconditions.nonNull(to, "to");
        long diffMs = to.getTime() - from.getTime();
        return TimeUnit.MILLISECONDS.toDays(diffMs);
    }


    private static Calendar normalizeToMidnight(long time) {
        Calendar calendar = calendarLocal.get();
        calendar.setTimeInMillis(time);
        calendar.clear(HOUR_OF_DAY);
        calendar.clear(AM_PM);
        calendar.clear(HOUR);
        calendar.clear(MINUTE);
        calendar.clear(SECOND);
        calendar.clear(MILLISECOND);
        return calendar;
    }

    public static Times.RelativeDate getRelativeDate(Date date, long now) {
        long target = date.getTime();
        Calendar midnight = normalizeToMidnight(now);

        midnight.add(DAY_OF_MONTH, 1);
        if (target > midnight.getTimeInMillis()) {
            return Times.RelativeDate.FUTURE;
        }
        midnight.add(DAY_OF_MONTH, -1);
        if (target >= midnight.getTimeInMillis()) {
            return Times.RelativeDate.TODAY;
        }
        midnight.add(DAY_OF_MONTH, -1);
        if (target >= midnight.getTimeInMillis()) {
            return Times.RelativeDate.YESTERDAY;
        }
        midnight.add(DAY_OF_MONTH, -5);
        return target >= midnight.getTimeInMillis() ? Times.RelativeDate.PAST_WEEK : Times.RelativeDate.OLDER;
    }

    public static Date getRelativeDate(Date date, int relativeDay) {
        long target = date.getTime();
        Calendar calendar = calendarLocal.get();
        calendar.setTimeInMillis(target);
        calendar.add(Calendar.DAY_OF_MONTH, relativeDay);
        return calendar.getTime();
    }

    public static enum RelativeDate {
        FUTURE,
        OLDER,
        PAST_WEEK,
        TODAY,
        YESTERDAY;
    }
}
