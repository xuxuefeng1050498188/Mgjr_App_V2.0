package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.MyJmgBean;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MyJmgModelImpl implements BaseModel {

    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {

        this.listener = listener;

        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.myJmgDataUrl();
        List<String> keyList = new ArrayList<>();
        keyList.add(APIBuilder.myJmgDataUrl());
        keyList.add(necessaryParams.get("mid"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<>();

        params.putAll(necessaryParams);
        if (unNecessaryParams != null) {
            params.putAll(unNecessaryParams);
        }
        HttpClient
                .get()
                .url(url)
                .params(params)
                .build()
                .execute(new CustomCallback());

    }

    private class CustomCallback extends Callback<MyJmgBean> {
        @Override
        public MyJmgBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            MyJmgBean myJmgBean = gson.fromJson(string, MyJmgBean.class);
            return myJmgBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(MyJmgBean response) {
            listener.onSuccess(response);
        }
    }
}
