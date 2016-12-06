package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.RegisterGetSmsCodeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
public class RegisterGetSmsCodePresenterImpl implements OnPresenterListener<GetVcodeBean> {

    private ViewListener  registerGetSmsCodeViewListener;
    private BaseModel baseModel;

    public RegisterGetSmsCodePresenterImpl(ViewListener registerGetSmsCodeViewListener) {
        this.registerGetSmsCodeViewListener = registerGetSmsCodeViewListener;
        baseModel = new RegisterGetSmsCodeModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        registerGetSmsCodeViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(GetVcodeBean bean) {
        registerGetSmsCodeViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            registerGetSmsCodeViewListener.responseData(this, bean);
        } else {
            registerGetSmsCodeViewListener.showError(this, bean);
        }
    }

    @Override
    public void onError() {
        registerGetSmsCodeViewListener.showError();
    }
}
