package com.mgjr.presenter.impl;

import com.mgjr.model.bean.RequestWithdrawBean;
import com.mgjr.model.impl.RequestRechargeModelImpl;
import com.mgjr.model.impl.RequestWithdrawModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class RequestWithdrawPresenterImpl implements OnPresenterListener<RequestWithdrawBean> {

    private ViewListener<RequestWithdrawBean> viewListener;
    private BaseModel baseModel;

    public RequestWithdrawPresenterImpl(ViewListener<RequestWithdrawBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new RequestWithdrawModelImpl();
    }

    @Override
    public void onSuccess(RequestWithdrawBean bean) {
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
