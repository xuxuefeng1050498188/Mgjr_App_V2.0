package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyJmgInvestDetailBean;
import com.mgjr.model.impl.InvestHomeModelImpl;
import com.mgjr.model.impl.MyJmgInvestDetailModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class MyJmgInvestDetailPresenterImpl implements OnPresenterListener<MyJmgInvestDetailBean> {

    private ViewListener<MyJmgInvestDetailBean> viewListener;
    private BaseModel baseModel;

    public MyJmgInvestDetailPresenterImpl(ViewListener<MyJmgInvestDetailBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new MyJmgInvestDetailModelImpl();
    }

    @Override
    public void onSuccess(MyJmgInvestDetailBean bean) {
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
