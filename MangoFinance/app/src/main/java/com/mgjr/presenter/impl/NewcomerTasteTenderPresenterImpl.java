package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyTyjBean;
import com.mgjr.model.impl.NewcomerTasteTenderModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/1.
 */
public class NewcomerTasteTenderPresenterImpl implements OnPresenterListener<MyTyjBean> {

    private ViewListener newcomerTasteTenderListener;
    private BaseModel baseModel;

    public NewcomerTasteTenderPresenterImpl(ViewListener newcomerTasteTenderListener) {
        this.newcomerTasteTenderListener = newcomerTasteTenderListener;
        baseModel = new NewcomerTasteTenderModelImpl();
    }

    public void sendRequest(Map<String,String> necessaryParams,Map<String,String> unNecessaryParams){
        newcomerTasteTenderListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }

    @Override
    public void onSuccess(MyTyjBean bean) {
        newcomerTasteTenderListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            newcomerTasteTenderListener.responseData(this,bean);
        }else {
            newcomerTasteTenderListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        newcomerTasteTenderListener.showError();

    }
}
