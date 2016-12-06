package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HqbSingleRedeemBean;
import com.mgjr.model.impl.HqbMutilRedeemConfirmModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */

public class HqbMutilRedeemConfirmPresenterImpl implements OnPresenterListener<HqbSingleRedeemBean> {
    private ViewListener hqbMutilRedeemConfirmViewListener;
    private BaseModel baseModel;

    public HqbMutilRedeemConfirmPresenterImpl(ViewListener hqbMutilRedeemConfirmViewListener) {
        this.hqbMutilRedeemConfirmViewListener = hqbMutilRedeemConfirmViewListener;
        baseModel = new HqbMutilRedeemConfirmModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        hqbMutilRedeemConfirmViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(HqbSingleRedeemBean bean) {
        hqbMutilRedeemConfirmViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            hqbMutilRedeemConfirmViewListener.responseData(this,bean);
        }else {
            hqbMutilRedeemConfirmViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        hqbMutilRedeemConfirmViewListener.showError();
    }
}
