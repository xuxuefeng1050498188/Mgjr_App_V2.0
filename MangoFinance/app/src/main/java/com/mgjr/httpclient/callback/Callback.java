package com.mgjr.httpclient.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wim on 16/5/26.
 */
public abstract class Callback<T> {

    public void onStart(Request request){

    }

    public void onComplete(){

    }

    public void inProgress(float progress){

    }

    public boolean validateReponse(Response response)
    {
        return response.isSuccessful();
    }

    public abstract T parseResponse(Response response) throws Exception;

    public abstract void onError(Call call, Exception e);

    public abstract void onResponse(T response);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object parseResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };
}
