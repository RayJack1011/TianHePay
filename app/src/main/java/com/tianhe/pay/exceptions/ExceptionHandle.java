package com.tianhe.pay.exceptions;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

/**
 * modify the source code from:
 * https://github.com/LRH1993/RetrofitRxJavaBox/blob/master/RetrofitClient/app/src/main/java/com/lvr/retrofitclient
 * /client/ExceptionHandle.java
 */
public class ExceptionHandle {

    public enum ErrorType {
        UNKNOWN(1000, "未知错误"),
        PARSE_ERROR(1001, "数据解析错误"),
        NETWORK_ERROR(1002, "网络连接失败"),
        HTTP_ERROR(1003, "网络错误"),
        SSL_ERROR(1004, "证书验证失败"),
        TIMEOUT_ERROR(1005, "连接超时");

        public final int errorCode;
        public final String message;

        ErrorType(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }

    public static ResponseThrowable handleError(Throwable error) {
        if (error instanceof HttpException) {
            return new ResponseThrowable(error, ((HttpException) error).code());
        }
        Throwable cause = error.getCause();
        if (cause == null) {
            return new ResponseThrowable(error, ErrorType.UNKNOWN);
        }
        for (Throwable proximateCause = cause;
             proximateCause != null;
             proximateCause = proximateCause.getCause()) {
            if (proximateCause instanceof JsonParseException) {
                return new ResponseThrowable(proximateCause, ErrorType.PARSE_ERROR);
            }
            if (proximateCause instanceof ConnectException) {
                return new ResponseThrowable(proximateCause, ErrorType.NETWORK_ERROR);
            }
            if (proximateCause instanceof SocketTimeoutException) {
                return new ResponseThrowable(proximateCause, ErrorType.TIMEOUT_ERROR);
            }
            if (proximateCause instanceof SSLHandshakeException) {
                return new ResponseThrowable(proximateCause, ErrorType.SSL_ERROR);
            }
            if (proximateCause.getCause() == proximateCause) {
                break;
            }
        }
        return new ResponseThrowable(error, ErrorType.UNKNOWN);
    }

    public static class ResponseThrowable extends Exception {
        public final ErrorType errorType;
        private final int httpStatusCode;

        public ResponseThrowable(Throwable throwable, ErrorType type) {
            super(throwable);
            this.errorType = type;
            this.httpStatusCode = 0;
        }

        public ResponseThrowable(Throwable throwable, int httpStatusCode) {
            super(throwable);
            this.errorType = ErrorType.HTTP_ERROR;
            this.httpStatusCode = httpStatusCode;
        }

        public boolean isHttpError() {
            return errorType == ErrorType.HTTP_ERROR;
        }

        public int getHttpStatusCode() {
            return httpStatusCode;
        }
    }

}
