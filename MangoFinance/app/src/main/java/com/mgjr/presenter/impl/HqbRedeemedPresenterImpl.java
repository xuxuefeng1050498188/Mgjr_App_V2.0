package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyHqbRunningProjectBean;
import com.mgjr.model.impl.HqbRedeemModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HqbRedeemedPresenterImpl implements OnPresenterListener<MyHqbRunningProjectBean> {
    private ViewListener hqbRedeemedViewListener;
    private BaseModel baseModel;

    public HqbRedeemedPresenterImpl(ViewListener hqbRedeemedViewListener) {
        this.hqbRedeemedViewListener = hqbRedeemedViewListener;
        baseModel = new HqbRedeemModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        hqbRedeemedViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyHqbRunningProjectBean bean) {
        hqbRedeemedViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            hqbRedeemedViewListener.responseData(this,bean);
        }else {
            hqbRedeemedViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        hqbRedeemedViewListener.showError();
    }
}
