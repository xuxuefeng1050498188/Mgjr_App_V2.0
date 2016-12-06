package com.mgjr.presenter.impl;

import com.mgjr.model.bean.AccountManageBean;
import com.mgjr.model.impl.AccountManageModelImpl;
import com.mgjr.model.impl.AccountSettingModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class AccountManagePresenterImpl implements OnPresenterListener<AccountManageBean> {

    private ViewListener<AccountManageBean> viewListener;
    private BaseModel baseModel;

    public AccountManagePresenterImpl(ViewListener<AccountManageBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new AccountManageModelImpl();
    }

    @Override
    public void onSuccess(AccountManageBean bean) {
        viewListener.hideLoading();
        if (bean != null && bean.getStatus().equalsIgnoreCase("0000")) {
            viewListener.responseData(this, bean);
        } else {
            viewListener.showError(this, bean);
        }
    }

    @Override
    public void onError() {
        viewListener.showError();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        viewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);

    }
}
