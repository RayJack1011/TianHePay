package com.tencent.common;

/**
 * User: rizenguo
 * Date: 2014/11/12
 * Time: 14:32
 */
public class Log {

    private static final String TAG = "tencent wechat";
    public static final String LOG_TYPE_TRACE = "logTypeTrace";
    public static final String LOG_TYPE_DEBUG = "logTypeDebug";
    public static final String LOG_TYPE_INFO = "logTypeInfo";
    public static final String LOG_TYPE_WARN = "logTypeWarn";
    public static final String LOG_TYPE_ERROR = "logTypeError";

    public void t(String s){
        android.util.Log.v(TAG, s);
    }

    public void d(String s){
        android.util.Log.d(TAG, s);
    }

    public void i(String s){
        android.util.Log.i(TAG, s);
    }

    public void w(String s){
        android.util.Log.w(TAG, s);
    }

    public void e(String s){
        android.util.Log.e(TAG, s);
    }

    public void log(String type, String s){
        if(type.equals(Log.LOG_TYPE_TRACE)){
            t(s);
        }else if(type.equals(Log.LOG_TYPE_DEBUG)){
            d(s);
        }else if(type.equals(Log.LOG_TYPE_INFO)){
            i(s);
        }else if(type.equals(Log.LOG_TYPE_WARN)){
            w(s);
        }else if(type.equals(Log.LOG_TYPE_ERROR)){
            e(s);
        }
    }

}
