package com.mgjr.presenter.impl;

import com.mgjr.model.bean.OptionCollectBean;
import com.mgjr.model.impl.OptionCollectModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/28.
 */

public class OptionCollectPresenteImpl implements OnPresenterListener<OptionCollectBean> {

    private ViewListener OptionCollectViewListener;
    private BaseModel baseModel;

    public OptionCollectPresenteImpl(ViewListener OptionCollectViewListener) {
        this.OptionCollectViewListener = OptionCollectViewListener;
        baseModel = new OptionCollectModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        OptionCollectViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(OptionCollectBean bean) {
        OptionCollectViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            OptionCollectViewListener.responseData(this,bean);
        }else {
            OptionCollectViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        OptionCollectViewListener.showError();
    }
}
