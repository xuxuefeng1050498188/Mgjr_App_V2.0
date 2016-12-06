package com.mgjr.presenter.impl;

import com.mgjr.model.bean.AccountSettingBean;
import com.mgjr.model.impl.AccountSettingModelImpl;
import com.mgjr.model.impl.RequestWithdrawModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class AccountSettingPresenterImpl implements OnPresenterListener<AccountSettingBean> {

    private ViewListener<AccountSettingBean> viewListener;
    private BaseModel baseModel;

    public AccountSettingPresenterImpl(ViewListener<AccountSettingBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new AccountSettingModelImpl();
    }

    @Override
    public void onSuccess(AccountSettingBean bean) {
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

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        viewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);

    }
}
