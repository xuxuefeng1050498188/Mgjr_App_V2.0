package com.mgjr.httpclient.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.model.bean.LoginBean;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wim on 16/8/16.
 */
public abstract class LoginCallback extends Callback<LoginBean> {
    @Override
    public LoginBean parseResponse(Response response) throws Exception {
        String string = response.body().string();
        Gson gson = new GsonBuilder().create();
        LoginBean loginBean = gson.fromJson(string,LoginBean.class);
        return loginBean;
    }


}
