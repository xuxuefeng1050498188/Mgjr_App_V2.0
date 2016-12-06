package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HqbSingleRedeemBean;
import com.mgjr.model.bean.HqbSingleRedeemComfirmBean;
import com.mgjr.model.impl.HqbSingleRedeemConfirmModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */

public class HqbSingleRedeemConfirmPresenterImpl implements OnPresenterListener<HqbSingleRedeemBean> {
    private ViewListener hqbSingleRedeemConfirmViewListener;
    private BaseModel baseModel;

    public HqbSingleRedeemConfirmPresenterImpl(ViewListener hqbSingleRedeemConfirmViewListener) {
        this.hqbSingleRedeemConfirmViewListener = hqbSingleRedeemConfirmViewListener;
        baseModel = new HqbSingleRedeemConfirmModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        hqbSingleRedeemConfirmViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(HqbSingleRedeemBean bean) {
        hqbSingleRedeemConfirmViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            hqbSingleRedeemConfirmViewListener.responseData(this,bean);
        }else {
            hqbSingleRedeemConfirmViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        hqbSingleRedeemConfirmViewListener.showError();
    }
}
