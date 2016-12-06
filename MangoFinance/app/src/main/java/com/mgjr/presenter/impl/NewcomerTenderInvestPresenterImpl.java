package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyTyjBean;
import com.mgjr.model.impl.NewcomerTenderInvestModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public class NewcomerTenderInvestPresenterImpl implements OnPresenterListener<MyTyjBean> {

    private ViewListener newcomerTenderInvestViewListener;
    private BaseModel baseModel;

    public NewcomerTenderInvestPresenterImpl(ViewListener newcomerTenderInvestViewListener) {
        this.newcomerTenderInvestViewListener = newcomerTenderInvestViewListener;
        baseModel = new NewcomerTenderInvestModelImpl();
    }

    public void sendRequest(Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
//        newcomerTenderInvestViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }

    @Override
    public void onSuccess(MyTyjBean bean) {
//        newcomerTenderInvestViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            newcomerTenderInvestViewListener.responseData(this,bean);
        }else {
            newcomerTenderInvestViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        newcomerTenderInvestViewListener.showError();
    }
}
