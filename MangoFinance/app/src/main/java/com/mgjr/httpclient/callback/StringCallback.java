package com.mgjr.httpclient.callback;

import okhttp3.Response;

/**
 * Created by wim on 16/5/26.
 */
public abstract class StringCallback extends Callback<String>{
    @Override
    public String parseResponse(Response response) throws Exception {
        return response.body().string();
    }


}
