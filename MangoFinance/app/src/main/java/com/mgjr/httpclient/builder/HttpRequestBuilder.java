package com.mgjr.httpclient.builder;

import com.mgjr.httpclient.request.RequestCall;

import java.util.Map;

/**
 * Created by wim on 16/5/26.
 */
public abstract class HttpRequestBuilder {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract HttpRequestBuilder url(String url);
    public abstract HttpRequestBuilder tag(Object tag);
    public abstract HttpRequestBuilder headers(Map<String, String> headers);
    public abstract HttpRequestBuilder addHeader(String key, String val);
    public abstract RequestCall build();
}
