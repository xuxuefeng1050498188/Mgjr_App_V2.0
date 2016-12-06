package com.mgjr.presenter.impl;

import com.mgjr.model.bean.APPVersion;
import com.mgjr.model.bean.AccountSettingBean;
import com.mgjr.model.impl.APPUpdateModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by wim on 16/10/24.
 */

public class APPUpdatePresenterImpl implements OnPresenterListener<APPVersion> {

    private ViewListener<APPVersion> viewListener;
    private BaseModel baseModel;

    public APPUpdatePresenterImpl(ViewListener<APPVersion> viewListener) {
        this.viewListener = viewListener;
        baseModel = new APPUpdateModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        viewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);

    }

    @Override
    public void onSuccess(APPVersion bean) {
        viewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            viewListener.responseData(this, bean);
        } else {
            viewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        viewListener.showError();
    }
}
