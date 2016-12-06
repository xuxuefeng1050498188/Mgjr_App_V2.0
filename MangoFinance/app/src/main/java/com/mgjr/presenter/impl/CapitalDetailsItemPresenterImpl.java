package com.mgjr.presenter.impl;

import com.mgjr.model.bean.CapitalDetailsBean;
import com.mgjr.model.impl.CapitalDetailsItemModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CapitalDetailsItemPresenterImpl implements OnPresenterListener<CapitalDetailsBean> {
    private ViewListener capitalDetailsItemPresenterViewListener;

    private BaseModel baseModel;

    public CapitalDetailsItemPresenterImpl(ViewListener capitalDetailsPresenterViewListener) {
        this.capitalDetailsItemPresenterViewListener = capitalDetailsPresenterViewListener;
        baseModel = new CapitalDetailsItemModelImpl();
    }

    public void sendRequest (Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
        capitalDetailsItemPresenterViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }

    @Override
    public void onSuccess(CapitalDetailsBean bean) {
        capitalDetailsItemPresenterViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            capitalDetailsItemPresenterViewListener.responseData(this,bean);
        }else {
            capitalDetailsItemPresenterViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        capitalDetailsItemPresenterViewListener.showError();
    }
}
