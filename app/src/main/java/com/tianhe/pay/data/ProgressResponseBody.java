package com.tianhe.pay.data;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private ProgressListener listener;

    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody,
                                ProgressListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long bytesRead = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.bytesRead += bytesRead == -1 ? 0 : bytesRead;
                listener.update(bytesRead, contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }

    class DownloadProgressInterceptor implements Interceptor {
        private ProgressListener progressListener;

        public DownloadProgressInterceptor(ProgressListener progressListener) {
            this.progressListener = progressListener;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
        }

    }

    public static interface ProgressListener {

        void update(long bytesRead, long contentLength, boolean done);

    }
}