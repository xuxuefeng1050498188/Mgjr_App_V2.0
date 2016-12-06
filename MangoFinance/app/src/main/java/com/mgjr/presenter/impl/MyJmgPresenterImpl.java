package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyJmgBean;
import com.mgjr.model.impl.InvestHomeModelImpl;
import com.mgjr.model.impl.MyJmgModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class MyJmgPresenterImpl implements OnPresenterListener<MyJmgBean> {

    private ViewListener<MyJmgBean> viewListener;
    private BaseModel baseModel;

    public MyJmgPresenterImpl(ViewListener<MyJmgBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new MyJmgModelImpl();
    }

    @Override
    public void onSuccess(MyJmgBean bean) {
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
