package com.mgjr.presenter.impl;

import com.mgjr.model.bean.AdBean;
import com.mgjr.model.bean.MangoBoxBean;
import com.mgjr.model.impl.AdModelImpl;
import com.mgjr.model.impl.MangoBoxModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */

public class AdPresenterImpl implements OnPresenterListener<AdBean> {


    private ViewListener adViewListener;
    private BaseModel baseModel;

    public AdPresenterImpl(ViewListener adViewListener) {
        this.adViewListener = adViewListener;
        baseModel = new AdModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        adViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(AdBean bean) {
        adViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            adViewListener.responseData(this, bean);
        } else {
            adViewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        adViewListener.showError();
    }
}
