package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.HqbSingleRedeemBean;
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
 * Created by Administrator on 2016/9/23.
 */

public class HqbMutilRedeemModelImpl implements BaseModel {
    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;
        String url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.hqbMutilRedeemed();

        List<String> keyList = new ArrayList<String>();
        keyList.add(APIBuilder.hqbMutilRedeemed());
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("tenderIds"));
//        keyList.add(necessaryParams.get("amount"));
//        keyList.add(necessaryParams.get("paypwd"));
        String keyStr = APIBuilder.getKeyStr(keyList);
        necessaryParams.put("keyStr", keyStr);

        Map<String, String> params = new HashMap<String, String>();
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

    private class CustomCallback extends Callback<HqbSingleRedeemBean> {
        @Override
        public HqbSingleRedeemBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            HqbSingleRedeemBean hqbMutilRedeemBean = gson.fromJson(string,HqbSingleRedeemBean.class);
            return hqbMutilRedeemBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(HqbSingleRedeemBean response) {
            listener.onSuccess(response);
        }
    }
}
