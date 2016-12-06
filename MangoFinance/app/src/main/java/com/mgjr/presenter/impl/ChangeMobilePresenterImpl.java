package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.ChangeMobileModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ChangeMobilePresenterImpl implements OnPresenterListener<GetVcodeBean> {
    private ViewListener ChangeMobileViewLisenter;

    private BaseModel baseModel;

    public ChangeMobilePresenterImpl(ViewListener ChangeMobileViewLisenter) {
        this.ChangeMobileViewLisenter = ChangeMobileViewLisenter;
        baseModel = new ChangeMobileModelImpl();
    }



    public void sendRequest (Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
        ChangeMobileViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(GetVcodeBean bean) {
        ChangeMobileViewLisenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            ChangeMobileViewLisenter.responseData(this,bean);
        }else {
            ChangeMobileViewLisenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        ChangeMobileViewLisenter.showError();
    }
}
