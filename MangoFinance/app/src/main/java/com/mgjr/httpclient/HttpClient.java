package com.mgjr.httpclient;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mgjr.Utils.Exceptions;
import com.mgjr.Utils.HttpsUtils;
import com.mgjr.httpclient.builder.GetBuilder;
import com.mgjr.httpclient.builder.PostFormBuilder;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.httpclient.cookie.CookieJarImpl;
import com.mgjr.httpclient.cookie.CookieStore;
import com.mgjr.httpclient.cookie.HasCookieStore;
import com.mgjr.httpclient.cookie.MemoryCookieStore;
import com.mgjr.httpclient.cookie.PersistentCookieStore;
import com.mgjr.httpclient.request.RequestCall;
import com.mgjr.mangofinance.MainApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by wim on 16/5/26.
 */
//http://192.168.30.5/V1.3/appHomeimg?keyStr=3aba364792758b7571cddde8321b2bd1
public class HttpClient {
    public static final long DEFAULT_MILLISECONDS = 10000;

    private static HttpClient mInstance;
    private OkHttpClient mOkHttpClient;

    private Handler mDelivery;

    public HttpClient(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //cookie enabled
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

//            mOkHttpClient = okHttpClientBuilder.certificatePinner(new CertificatePinner.Builder()
//                    .add("app.hnmgjr.com", "sha256/afwiKY3RxoMmLkuRW1l7QsPZTJPwDS2pdDROQjXw8ig=")
//                    .build()).build();
            mOkHttpClient = okHttpClientBuilder.build();
        } else {
            mOkHttpClient = okHttpClient;
        }

        init();
    }



    private void init() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public HttpClient debug(String tag) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(new LoggerInterceptor(tag, false)).build();
        return this;
    }

    public static HttpClient getInstance(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new HttpClient(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static HttpClient getInstance() {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new HttpClient(null);
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    Object obj = finalCallback.parseResponse(response);
                    sendSuccessResultCallback(obj, finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                }
            }
        });
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onComplete();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onComplete();
            }
        });
    }

    public CookieStore getCookieStore() {
        final CookieJar cookieJar = mOkHttpClient.cookieJar();
        if (cookieJar == null) {
            Exceptions.illegalArgument("you should invoked okHttpClientBuilder.cookieJar() to set a cookieJar.");
        }
        if (cookieJar instanceof HasCookieStore) {
            return ((HasCookieStore) cookieJar).getCookieStore();
        } else {
            return null;
        }
    }


    public void setCertificates(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(certificates, null, null);

        OkHttpClient.Builder builder = getOkHttpClient().newBuilder();
        builder = builder.sslSocketFactory(sslSocketFactory);
        mOkHttpClient = builder.build();
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, bksFile, password))
                .build();
    }

    public void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .hostnameVerifier(hostNameVerifier)
                .build();
    }

    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .connectTimeout(timeout, units)
                .build();
    }

    public void setReadTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .readTimeout(timeout, units)
                .build();
    }

    public void setWriteTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .writeTimeout(timeout, units)
                .build();
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

}
