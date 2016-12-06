package com.mgjr.presenter.impl;

import com.mgjr.model.bean.RegisterBean;
import com.mgjr.model.impl.RegisterStepTwoModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
public class RegisterStepTwoPresenterImpl implements OnPresenterListener<RegisterBean> {

    private ViewListener registerStepTwoViewListener;
    private BaseModel baseModel;

    public RegisterStepTwoPresenterImpl(ViewListener registerStepTwoViewListener) {
        this.registerStepTwoViewListener = registerStepTwoViewListener;
        baseModel = new RegisterStepTwoModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        registerStepTwoViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(RegisterBean bean) {
        registerStepTwoViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            registerStepTwoViewListener.responseData(this, bean);
        } else {
            registerStepTwoViewListener.showError(this, bean);
        }
    }

    @Override
    public void onError() {
        registerStepTwoViewListener.showError();
    }
}
