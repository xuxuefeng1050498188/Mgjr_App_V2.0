package com.mgjr.httpclient.builder;

import java.util.Map;

/**
 * Created by wim on 16/5/26.
 */
public interface HasParamsable {
    public abstract HttpRequestBuilder params(Map<String, String> params);

    public abstract HttpRequestBuilder addParams(String key, String value);

}
