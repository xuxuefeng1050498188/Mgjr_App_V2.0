package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HqbSingleRedeemBean;
import com.mgjr.model.impl.HqbSingleRedeemModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */

public class HqbSingleRedeemPresenterImpl implements OnPresenterListener<HqbSingleRedeemBean> {
    private ViewListener hqbSingleRedeemViewListener;
    private BaseModel baseModel;

    public HqbSingleRedeemPresenterImpl(ViewListener hqbSingleRedeemViewListener) {
        this.hqbSingleRedeemViewListener = hqbSingleRedeemViewListener;
        baseModel = new HqbSingleRedeemModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        hqbSingleRedeemViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(HqbSingleRedeemBean bean) {
        hqbSingleRedeemViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            hqbSingleRedeemViewListener.responseData(this,bean);
        }else {
            hqbSingleRedeemViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        hqbSingleRedeemViewListener.showError();
    }
}
