package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MangoBoxBean;
import com.mgjr.model.impl.MangoBoxModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MangoBoxPresenterImpl implements OnPresenterListener<MangoBoxBean> {

    private ViewListener mangoBoxViewListener;
    private BaseModel baseModel;

    public MangoBoxPresenterImpl(ViewListener mangoBoxViewListener) {
        this.mangoBoxViewListener = mangoBoxViewListener;
        baseModel = new MangoBoxModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        mangoBoxViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(MangoBoxBean bean) {
        mangoBoxViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            mangoBoxViewListener.responseData(this, bean);
        } else {
            mangoBoxViewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        mangoBoxViewListener.showError();
    }
}
