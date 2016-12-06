package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.LogUtil;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.BankCardManageBean;
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
 * Created by xuxuefeng on 2016/8/22.
 */
public class RequestBankCardInfoModelImpl implements BaseModel {
    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;

        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.requestBankCardInfoUrl();

        List<String> keyList = new ArrayList<String>();
        keyList.add(APIBuilder.requestBankCardInfoUrl());
        keyList.add(necessaryParams.get("mid"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
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

    private class CustomCallback extends Callback<BankCardManageBean> {
        @Override
        public BankCardManageBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            LogUtil.e(string);
            Gson gson = new GsonBuilder().create();
            //测试
            BankCardManageBean bankCardManageBean = gson.fromJson(string, BankCardManageBean.class);
            return bankCardManageBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(BankCardManageBean response) {
            listener.onSuccess(response);
        }
    }
}
