package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.FinancialPlannerBean;
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
 * Created by Administrator on 2016/10/8.
 */

public class FinancialPlannerModelImpl implements BaseModel {

    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;

        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.appFinancialPlanner();
        List<String> keyList = new ArrayList<>();
        keyList.add(APIBuilder.appFinancialPlanner());
        keyList.add(necessaryParams.get("mid"));
        String keyStr = APIBuilder.getKeyStr(keyList);

        necessaryParams.put("keyStr",keyStr);

        Map<String,String> params = new HashMap<>();
        params.putAll(necessaryParams);
        if(unNecessaryParams != null){
            params.putAll(unNecessaryParams);
        }
        HttpClient
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new CustomCallback());

    }

    private class CustomCallback extends Callback<FinancialPlannerBean> {
        @Override
        public FinancialPlannerBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            FinancialPlannerBean financialPlannerBean = gson.fromJson(string,FinancialPlannerBean.class);
            return financialPlannerBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(FinancialPlannerBean response) {
            listener.onSuccess(response);
        }
    }
}
