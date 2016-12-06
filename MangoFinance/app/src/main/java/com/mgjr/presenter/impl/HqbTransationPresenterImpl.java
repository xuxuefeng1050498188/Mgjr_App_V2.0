package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HqbTransactionBean;
import com.mgjr.model.impl.HqbTransactionModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HqbTransationPresenterImpl implements OnPresenterListener<HqbTransactionBean> {
    private ViewListener HqbTransationViewListener;
    private BaseModel baseModel;

    public HqbTransationPresenterImpl(ViewListener HqbTransationViewListener) {
        this.HqbTransationViewListener = HqbTransationViewListener;
        baseModel = new HqbTransactionModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        HqbTransationViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(HqbTransactionBean bean) {
        HqbTransationViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            HqbTransationViewListener.responseData(this, bean);
        } else {
            HqbTransationViewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        HqbTransationViewListener.showError();
    }

}
