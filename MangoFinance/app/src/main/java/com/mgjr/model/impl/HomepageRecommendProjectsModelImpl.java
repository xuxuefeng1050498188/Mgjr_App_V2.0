package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.HomepageRecommendProjectsBean;
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
 * Created by Administrator on 2016/9/14.
 */
public class HomepageRecommendProjectsModelImpl implements BaseModel {
    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {

        this.listener = listener;
        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.homepageRecommendProjects();

        List<String> keyList = new ArrayList<>();
        keyList.add(APIBuilder.homepageRecommendProjects());
        String keyStr = APIBuilder.getKeyStr(keyList);
        if(necessaryParams == null){

            necessaryParams = new HashMap<>();
        }
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

    private class CustomCallback extends Callback<HomepageRecommendProjectsBean> {
        @Override
        public HomepageRecommendProjectsBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            HomepageRecommendProjectsBean homepageRecommendProjectsBean = gson.fromJson(string, HomepageRecommendProjectsBean.class);
            return homepageRecommendProjectsBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(HomepageRecommendProjectsBean response) {
            listener.onSuccess(response);
        }
    }
}
