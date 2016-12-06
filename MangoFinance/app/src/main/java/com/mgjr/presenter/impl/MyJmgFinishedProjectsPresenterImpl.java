package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyJmgInvestingProjectsBean;
import com.mgjr.model.impl.MyJmgFinishedProjectsModelImpl;
import com.mgjr.model.impl.MyJmgInvestingProjectsModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class MyJmgFinishedProjectsPresenterImpl implements OnPresenterListener<MyJmgInvestingProjectsBean> {

    private ViewListener<MyJmgInvestingProjectsBean> viewListener;
    private BaseModel baseModel;

    public MyJmgFinishedProjectsPresenterImpl(ViewListener<MyJmgInvestingProjectsBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new MyJmgFinishedProjectsModelImpl();
    }

    @Override
    public void onSuccess(MyJmgInvestingProjectsBean bean) {
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
