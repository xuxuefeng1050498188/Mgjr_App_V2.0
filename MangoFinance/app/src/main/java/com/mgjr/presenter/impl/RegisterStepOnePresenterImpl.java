package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.RegisterStepOneModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class RegisterStepOnePresenterImpl implements OnPresenterListener<GetVcodeBean> {

    private ViewListener registerStepOneViewListener;
    private BaseModel baseModel;

    public RegisterStepOnePresenterImpl(ViewListener registerStepOneViewListener) {
        this.registerStepOneViewListener = registerStepOneViewListener;
        baseModel = new RegisterStepOneModelImpl();

    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        registerStepOneViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(GetVcodeBean bean) {
        registerStepOneViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            registerStepOneViewListener.responseData(this, bean);
        } else {
            registerStepOneViewListener.showError(this, bean);
        }
    }

    @Override
    public void onError() {
        registerStepOneViewListener.showError();
    }
}
