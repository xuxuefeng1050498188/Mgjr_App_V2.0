package com.mgjr.presenter.impl;

import com.mgjr.model.bean.TenderRewardListBean;
import com.mgjr.model.impl.TenderRewardListModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/10.
 */

public class TenderRewardListPresenterImpl implements OnPresenterListener<TenderRewardListBean> {

    private ViewListener TenderRewardListViewListener;
    private BaseModel baseModel;

    public TenderRewardListPresenterImpl(ViewListener TenderRewardListViewListener) {
        this.TenderRewardListViewListener = TenderRewardListViewListener;
        baseModel = new TenderRewardListModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        TenderRewardListViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(TenderRewardListBean bean) {
        TenderRewardListViewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            TenderRewardListViewListener.responseData(this, bean);
        } else {
            TenderRewardListViewListener.showError(this, bean);
        }
    }

    @Override
    public void onError() {
        TenderRewardListViewListener.showError();
    }
}
