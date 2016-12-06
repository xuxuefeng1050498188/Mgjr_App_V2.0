package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyJmgTransactionDetailsBean;
import com.mgjr.model.impl.InvestHomeModelImpl;
import com.mgjr.model.impl.MyJmgTransactionDetailModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class MyJmgTransationDetailPresenterImpl implements OnPresenterListener<MyJmgTransactionDetailsBean> {

    private ViewListener<MyJmgTransactionDetailsBean> viewListener;
    private BaseModel baseModel;

    public MyJmgTransationDetailPresenterImpl(ViewListener<MyJmgTransactionDetailsBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new MyJmgTransactionDetailModelImpl();
    }

    @Override
    public void onSuccess(MyJmgTransactionDetailsBean bean) {
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
