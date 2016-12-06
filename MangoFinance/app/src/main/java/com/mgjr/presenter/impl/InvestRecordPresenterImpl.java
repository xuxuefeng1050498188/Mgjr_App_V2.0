package com.mgjr.presenter.impl;

import com.mgjr.model.bean.InvestRecordBean;
import com.mgjr.model.impl.InvestHomeModelImpl;
import com.mgjr.model.impl.InvestRecordModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class InvestRecordPresenterImpl implements OnPresenterListener<InvestRecordBean> {

    private ViewListener<InvestRecordBean> viewListener;
    private BaseModel baseModel;

    public InvestRecordPresenterImpl(ViewListener<InvestRecordBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new InvestRecordModelImpl();
    }

    @Override
    public void onSuccess(InvestRecordBean bean) {
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
