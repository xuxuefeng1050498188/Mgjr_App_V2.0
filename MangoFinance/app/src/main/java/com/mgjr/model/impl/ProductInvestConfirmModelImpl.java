package com.mgjr.model.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.Callback;
import com.mgjr.model.bean.ProductInvestConfirmBean;
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
public class ProductInvestConfirmModelImpl implements BaseModel {
    OnPresenterListener listener;

    @Override
    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener) {
        this.listener = listener;


        //判断是请求金芒果还是活期宝
        String url = null;
        String type = necessaryParams.get("type");
        if ("0".equals(type)) {
            //活期宝
            url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.hqbInvestConfirmUrl();
        } else {
            //金芒果
            url = APIBuilder.baseUrl + APIBuilder.version + APIBuilder.jmgInvestConfirmUrl();
        }

        //删除判断产品类型字段
        necessaryParams.remove("type");

        List<String> keyList = new ArrayList<String>();
        if ("0".equals(type)) {
            keyList.add(APIBuilder.hqbInvestConfirmUrl());
        } else {
            keyList.add(APIBuilder.jmgInvestConfirmUrl());
        }
        keyList.add(necessaryParams.get("mid"));
        keyList.add(necessaryParams.get("code"));
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

    private class CustomCallback extends Callback<ProductInvestConfirmBean> {
        @Override
        public ProductInvestConfirmBean parseResponse(Response response) throws Exception {
            String string = response.body().string();
            PhoneUtils.setSystemErrorReason(string);
            Gson gson = new GsonBuilder().create();
            ProductInvestConfirmBean productInvestConfirmBean = gson.fromJson(string, ProductInvestConfirmBean.class);
            return productInvestConfirmBean;
        }

        @Override
        public void onError(Call call, Exception e) {
            PhoneUtils.saveSystemErrorInfo(e);
            listener.onError();
        }

        @Override
        public void onResponse(ProductInvestConfirmBean response) {
            listener.onSuccess(response);
        }
    }
}
