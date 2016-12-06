package com.mgjr.presenter.impl;

import com.mgjr.model.bean.UserCenterBean;
import com.mgjr.model.impl.UserCenterModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/26.
 */
public class UserCenterPresenterImpl implements OnPresenterListener<UserCenterBean> {

    private ViewListener userCenterPresenter;
    private BaseModel baseModel;

    public UserCenterPresenterImpl(ViewListener userCenterPresenter) {
        this.userCenterPresenter = userCenterPresenter;
        baseModel = new UserCenterModelImpl();
    }


    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        userCenterPresenter.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(UserCenterBean bean) {
        userCenterPresenter.hideLoading();
        if (bean.getStatus() != null) {
            if (bean.getStatus().equalsIgnoreCase("0000")) {
                userCenterPresenter.responseData(this, bean);
            } else {
                userCenterPresenter.showError(this, bean);
            }
        }
    }

    @Override
    public void onError() {

        userCenterPresenter.showError();
    }
}
