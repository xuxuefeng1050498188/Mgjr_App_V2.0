package com.mgjr.presenter.impl;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.impl.InvestHomeModelImpl;
import com.mgjr.model.impl.ProfileMyinfosUploadNicknameModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class ProfileMyinfosUploadNicknamePresenterImpl implements OnPresenterListener<BaseBean> {

    private ViewListener<BaseBean> viewListener;
    private BaseModel baseModel;

    public ProfileMyinfosUploadNicknamePresenterImpl(ViewListener<BaseBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new ProfileMyinfosUploadNicknameModelImpl();
    }

    @Override
    public void onSuccess(BaseBean bean) {
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
