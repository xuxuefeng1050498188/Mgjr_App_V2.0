package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.LoginModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by wim on 16/8/16.
 */
public class LoginPresenterImpl implements OnPresenterListener<GetVcodeBean> {

    private ViewListener loginViewListener;
    private BaseModel baseModel;

    public LoginPresenterImpl(ViewListener loginViewListener) {
        this.loginViewListener = loginViewListener;
        baseModel = new LoginModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        loginViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(GetVcodeBean bean) {
        GetVcodeBean loginBean = (GetVcodeBean) bean;
        loginViewListener.hideLoading();
        if (loginBean.getStatus().equalsIgnoreCase("0000")) {
            loginViewListener.responseData(this, loginBean);
        } else {
            loginViewListener.showError(this, loginBean);
        }
    }

    @Override
    public void onError() {
        loginViewListener.hideLoading();
        loginViewListener.showError();
    }
}
