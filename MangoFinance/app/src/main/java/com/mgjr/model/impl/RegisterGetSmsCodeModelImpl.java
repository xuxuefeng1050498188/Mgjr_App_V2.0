package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.GetVcodeBean;
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
 * Created by Administrator on 2016/8/23.
 */
public class RegisterGetSmsCodeModelImpl implements BaseModel {

    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;
        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.registerGetSmsCodeUrl();
        List<String> keyList = new ArrayList<>();
        keyList.add(APIBuilder.registerGetSmsCodeUrl());
        keyList.add(necessaryParams.get("mobile"));
        keyList.add(necessaryParams.get("action"));

        String keyStr = APIBuilder.getKeyStr(keyList);


        necessaryParams.put("keyStr", keyStr);
        Map<String, String> params = new HashMap<>();
        params.putAll(necessaryParams);
        if (unNecessaryParams != null) {
            params.putAll(unNecessaryParams);
        }
        HttpClient.
                post().
                url(url).
                params(params).
                build().
                execute(new CustomCallback());

    }

    private class CustomCallback extends Callback<GetVcodeBean> {

        @Override
        public GetVcodeBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            GetVcodeBean registerSmsBean = gson.fromJson(string, GetVcodeBean.class);
            return registerSmsBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(GetVcodeBean response) {
            listener.onSuccess(response);
            String status = response.getStatus();
        }
    }
}
