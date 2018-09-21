package com.tianhe.pay.di.module;

import android.content.Context;
import android.util.Log;

import com.ali.AliResult;
import com.ali.AliResultJsonDeserializer;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tencent.common.Configure;
import com.tianhe.pay.R;
import com.tianhe.pay.data.Md5Sign;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.Skip;
import com.tianhe.pay.data.TianheSign;
import com.tianhe.pay.utils.money.Money;
import com.tianhe.pay.utils.money.MoneyGsonAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    // 实际上根本没用, 但是Retrofit必须设置一个
    private static final String HOST_RELEASE = "http://www.baidu.com";

    private static final int TIMEOUT = 5;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(SSLSocketFactory sslSocketFactory) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory)
                .addNetworkInterceptor(new HeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        return client;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(
                        TypeAdapters.newFactory(Money.class, new MoneyGsonAdapter()))
                .registerTypeAdapter(AliResult.class, new AliResultJsonDeserializer())
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        Skip skip = f.getAnnotation(Skip.class);
                        if (skip != null) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Provides
    @Singleton
    @Named("md5Sign")
    Sign provideMd5Sign() {
        return new Md5Sign();
    }

    @Provides
    @Singleton
    @Named("tianheSign")
    Sign provideTianheSign() {
        return new TianheSign();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(HOST_RELEASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        return retrofit;
    }

//    public SSLSocketFactory providerSSLSocketFactory(Context context) {
//        InputStream keyInput = context.getResources().openRawResource(R.raw.apiclient_cert);
//        try {
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            keyStore.load(keyInput, "1321365901".toCharArray());
//            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            kmfactory.init(keyStore, "1321365901".toCharArray());
//            SSLContext sslContext = SSLContext.getInstance("TLSv1");
//            sslContext.init(kmfactory.getKeyManagers(), null, null);
//            return sslContext.getSocketFactory();
//        } catch (KeyStoreException e) {
//        } catch (CertificateException e) {
//        } catch (NoSuchAlgorithmException e) {
//        } catch (IOException e) {
//        } catch (UnrecoverableKeyException e) {
//        } catch (KeyManagementException e) {
//        } finally {
//            try {
//                if (keyInput != null) {
//                    keyInput.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    @Provides
    @Singleton
    public static SSLSocketFactory providerSSLSocketFactory(Context context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        return createTencentSSLSocketFactory(context);
    }

    private static SSLSocketFactory createAliSslSocketFactory(Context context) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            int[] certificates = new int[]{R.raw.tianhe_apiclient_cert};
            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (CertificateException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (KeyStoreException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (IOException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (KeyManagementException e) {
            Log.e("SSLSocketFactory", e.toString());
        }
        return null;
    }

    private static SSLSocketFactory createTencentSSLSocketFactory(Context context) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = context.getResources().openRawResource(R.raw.tianhe_apiclient_cert);
            try {
                // 设置证书密码
                keyStore.load(instream, Configure.getCertPassword().toCharArray());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                instream.close();
            }
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keyStore, Configure.getCertPassword().toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(kmfactory.getKeyManagers(), null, null);
            return sslContext.getSocketFactory();
        } catch (KeyStoreException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (IOException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (KeyManagementException e) {
            Log.e("SSLSocketFactory", e.toString());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
//                    .header("Content-Type", "text/json;charset=UTF-8")
                    .header("Connection", "close")   // http1.1默认是长链接,不关闭就会报IOE
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }
}
