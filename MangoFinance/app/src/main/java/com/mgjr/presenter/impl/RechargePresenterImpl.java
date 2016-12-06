package com.mgjr.presenter.impl;

import com.mgjr.model.bean.RequestRechargeBean;
import com.mgjr.model.impl.RechargeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class RechargePresenterImpl implements OnPresenterListener<RequestRechargeBean> {

    private ViewListener<RequestRechargeBean> viewListener;
    private BaseModel baseModel;

    public RechargePresenterImpl(ViewListener<RequestRechargeBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new RechargeModelImpl();
    }

    @Override
    public void onSuccess(RequestRechargeBean bean) {
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
