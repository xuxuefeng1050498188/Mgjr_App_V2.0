package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HqbTransactionBean;
import com.mgjr.model.impl.JmgTransationModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */

public class JmgTransationPresenterImpl implements OnPresenterListener<HqbTransactionBean> {

    private ViewListener JmgTransationViewListener;
    private BaseModel baseModel;

    public JmgTransationPresenterImpl(ViewListener JmgTransationViewListener) {
        this.JmgTransationViewListener = JmgTransationViewListener;
        baseModel = new JmgTransationModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        JmgTransationViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(HqbTransactionBean bean) {
        JmgTransationViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            JmgTransationViewListener.responseData(this, bean);
        } else {
            JmgTransationViewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        JmgTransationViewListener.showError();
    }

}
