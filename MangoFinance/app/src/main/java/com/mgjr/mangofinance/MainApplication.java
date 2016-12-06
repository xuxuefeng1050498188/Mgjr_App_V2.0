package com.mgjr.mangofinance;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.mgjr.Utils.CrashHandler;
import com.mgjr.httpclient.HttpClient;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okio.Buffer;


public class MainApplication extends Application {

    private static Context mContext;

    private static Resources resources;

    private String CER = "-----BEGIN CERTIFICATE-----\n" +
            "MIIGmzCCBYOgAwIBAgIQNObhAtLhXXT6lt4lFLSbYjANBgkqhkiG9w0BAQsFADB3MQswCQYDVQQG\n" +
            "EwJVUzEdMBsGA1UEChMUU3ltYW50ZWMgQ29ycG9yYXRpb24xHzAdBgNVBAsTFlN5bWFudGVjIFRy\n" +
            "dXN0IE5ldHdvcmsxKDAmBgNVBAMTH1N5bWFudGVjIENsYXNzIDMgRVYgU1NMIENBIC0gRzMwHhcN\n" +
            "MTYwMzA2MDAwMDAwWhcNMTYxMTE4MjM1OTU5WjCCARIxEzARBgsrBgEEAYI3PAIBAxMCQ04xFjAU\n" +
            "BgsrBgEEAYI3PAIBAhMFSHVuYW4xGTAXBgsrBgEEAYI3PAIBARMIQ2hhbmdzaGExHTAbBgNVBA8T\n" +
            "FFByaXZhdGUgT3JnYW5pemF0aW9uMRgwFgYDVQQFEw80MzAwMDAwMDAxMTU3NjYxCzAJBgNVBAYT\n" +
            "AkNOMQ8wDQYDVQQIDAbmuZbljZcxDzANBgNVBAcMBumVv+aymTEzMDEGA1UECgwq5rmW5Y2X6IqS\n" +
            "5p6c6LSi5a+M572R57uc56eR5oqA5pyJ6ZmQ5YWs5Y+4MRIwEAYDVQQLDAnmioDmnK/pg6gxFzAV\n" +
            "BgNVBAMMDnd3dy5obm1nanIuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuGX+\n" +
            "he6aWQu+hJKuzM7p6I0SFc6N4YKjKNo4ihSBVYBn0+yG/wiAClQHZhCX4l15QqqkAzrvCBngBE4g\n" +
            "TcxbJpjWkNsh4BPRnDnGAg/BdeVcYzi5toGTUhu2F6UqRsa0hUKsbSLwMYPowCizQRVe+iaSe+jS\n" +
            "1rBZttLqVnpLojGpgM58kI9/7C+86nymwJfcDzVzaXqbXnH81I42/VutBjXmIkU3fp5PMfjIaOOy\n" +
            "stKQLeRizkVkBeFyVODE0Ivn2E0TVv6VQItONoAKX+8Isx3r+dco7ERar5eXEEYfQp1fjK1fehXa\n" +
            "r4aqkDHZLVeflPMaOU1oK8Ch8hePm6yDRQIDAQABo4IChDCCAoAwJQYDVR0RBB4wHIIOd3d3Lmhu\n" +
            "bWdqci5jb22CCmhubWdqci5jb20wCQYDVR0TBAIwADAOBgNVHQ8BAf8EBAMCBaAwbwYDVR0gBGgw\n" +
            "ZjBbBgtghkgBhvhFAQcXBjBMMCMGCCsGAQUFBwIBFhdodHRwczovL2Quc3ltY2IuY29tL2NwczAl\n" +
            "BggrBgEFBQcCAjAZDBdodHRwczovL2Quc3ltY2IuY29tL3JwYTAHBgVngQwBATArBgNVHR8EJDAi\n" +
            "MCCgHqAchhpodHRwOi8vc3Iuc3ltY2IuY29tL3NyLmNybDAdBgNVHSUEFjAUBggrBgEFBQcDAQYI\n" +
            "KwYBBQUHAwIwHwYDVR0jBBgwFoAUAVmr5906C1mmZGPWzyAHV9WR52owVwYIKwYBBQUHAQEESzBJ\n" +
            "MB8GCCsGAQUFBzABhhNodHRwOi8vc3Iuc3ltY2QuY29tMCYGCCsGAQUFBzAChhpodHRwOi8vc3Iu\n" +
            "c3ltY2IuY29tL3NyLmNydDCCAQMGCisGAQQB1nkCBAIEgfQEgfEA7wB1AN3rHSt6DU+mIIuBrYFo\n" +
            "cH4ujp0B1VyIjT0RxM227L7MAAABU0pQeWYAAAQDAEYwRAIgYrzZkyK9ozBTZHClKBcj3S9ohg84\n" +
            "x4VltSJ85p9zAbMCIBFXGd0Ocw9W0Oo4MrH+bV0CeEdN+jwhd+aJ2dNoHyFzAHYApLkJkLQYWBSH\n" +
            "uxOizGdwCjw1mAT5G9+443fNDsgN3BAAAAFTSlB5mQAABAMARzBFAiBIgXKd7IIIfKdlH8IzD8Fl\n" +
            "fz4akMPJGC5TKAgLlgvSQQIhAMDjdx0dCi7ge6HLTW+PZYjhepKS72waYdsbA34hPWi7MA0GCSqG\n" +
            "SIb3DQEBCwUAA4IBAQAr67ogGLoxyfLilAFwT9IA2NyHqemVtUq+Hev+lFZMjl9LJ6mvoHb6Ykv5\n" +
            "dpLcROjq4G/86ZR2zfYDql+kjbElo++AvhKrb6auMGhsLxP1v6029Cn9Awmdpu/3Eu1MfsiYhXri\n" +
            "QwcvVV0zYsm+nG0ayDteCJYmRIytdYCwj+41XEmBuiOtr5XFNanbywTwgVyhzLpSr6odZmgfipUx\n" +
            "T0JLSCxNFv9XrsSnp1959yJCe9fggTT0qEhzB/T8nj+4yqKp5NEV+7y8V42B4UtTA2iq24Jekcjt\n" +
            "21dPm2kKSxV8lZOoFaBmUpHuiNVqw/QRV41SA2KDgyXeO24NDA3yqkMx\n" +
            "-----END CERTIFICATE-----";

    @Override
    public void onCreate() {
        super.onCreate();
        //利用第三方库,修改全局字体
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/huawenxingkai.ttf").setFontAttrId(R.attr.fontPath).build());
        mContext = getApplicationContext();
        resources = getResources();
        HttpClient.getInstance().setCertificates(new InputStream[]{
                new Buffer()
                        .writeUtf8(CER)
                        .inputStream()});

        HttpClient.getInstance().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        /*初始化极光推送服务*/
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        initCatchCrash();
        //友盟调试模式
        MobclickAgent.setDebugMode(false);
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(true);
        // 设置统计场景类型为普通统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //错误日志
        MobclickAgent.setCatchUncaughtExceptions(false);

    }


    public static Context getContext() {
        return mContext;
    }

    public static Resources resources() {
        return resources;
    }

    private void initCatchCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setCrashHandler(getApplicationContext());
//        crashHandler.getCrashFile();
    }


}
