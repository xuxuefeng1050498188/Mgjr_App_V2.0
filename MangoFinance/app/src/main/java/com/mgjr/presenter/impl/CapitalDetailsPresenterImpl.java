package com.mgjr.presenter.impl;

import com.mgjr.model.bean.CapitalDetailsBean;
import com.mgjr.model.impl.CapitalDetailsModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class CapitalDetailsPresenterImpl implements OnPresenterListener<CapitalDetailsBean> {

    private ViewListener capitalDetailsPresenterViewListener;

    private BaseModel baseModel;

    public CapitalDetailsPresenterImpl(ViewListener capitalDetailsPresenterViewListener) {
        this.capitalDetailsPresenterViewListener = capitalDetailsPresenterViewListener;
        baseModel = new CapitalDetailsModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        capitalDetailsPresenterViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(CapitalDetailsBean bean) {
        capitalDetailsPresenterViewListener.hideLoading();
        if (bean.getStatus() != null) {
            if (bean.getStatus().equalsIgnoreCase("0000")) {
                capitalDetailsPresenterViewListener.responseData(this, bean);
            } else {
                capitalDetailsPresenterViewListener.showError(this, bean);
            }
        }
    }

    @Override
    public void onError() {
        capitalDetailsPresenterViewListener.showError();
    }
}
