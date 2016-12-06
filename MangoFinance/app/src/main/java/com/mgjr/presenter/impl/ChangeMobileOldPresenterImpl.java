package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.ChangeMobileOldModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ChangeMobileOldPresenterImpl implements OnPresenterListener<GetVcodeBean> {
    private ViewListener ChangeMobileOldViewLisenter;

    private BaseModel baseModel;

    public ChangeMobileOldPresenterImpl(ViewListener ChangeMobileOldViewLisenter) {
        this.ChangeMobileOldViewLisenter = ChangeMobileOldViewLisenter;
        baseModel = new ChangeMobileOldModelImpl();
    }



    public void sendRequest (Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
        ChangeMobileOldViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(GetVcodeBean bean) {
        ChangeMobileOldViewLisenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            ChangeMobileOldViewLisenter.responseData(this,bean);
        }else {
            ChangeMobileOldViewLisenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        ChangeMobileOldViewLisenter.showError();
    }
}
