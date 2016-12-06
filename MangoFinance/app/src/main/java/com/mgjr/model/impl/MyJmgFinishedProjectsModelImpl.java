package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.MyJmgInvestingProjectsBean;
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
public class MyJmgFinishedProjectsModelImpl implements BaseModel {
    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;

        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.myJmgFinishedProjectsUrl();

        List<String> keyList = new ArrayList<String>();
        keyList.add(APIBuilder.myJmgFinishedProjectsUrl());
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("pageNum"));
        keyList.add(necessaryParams.get("pageSize"));
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

    private class CustomCallback extends Callback<MyJmgInvestingProjectsBean> {
        @Override
        public MyJmgInvestingProjectsBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            MyJmgInvestingProjectsBean myJmgFinishedProjectsBean = gson.fromJson(string, MyJmgInvestingProjectsBean.class);
            return myJmgFinishedProjectsBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(MyJmgInvestingProjectsBean response) {
            listener.onSuccess(response);
//            LogUtil.e("获取数据成功");
        }
    }
}
