package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyTyjBean;
import com.mgjr.model.impl.MyTyjModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MyTyjPresenterImpl implements OnPresenterListener<MyTyjBean> {

    private ViewListener myTyjPresenter;
    private BaseModel baseModel;

    public MyTyjPresenterImpl(ViewListener myTyjPresenter) {
        this.myTyjPresenter = myTyjPresenter;
        baseModel = new MyTyjModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        myTyjPresenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyTyjBean bean) {
        myTyjPresenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            myTyjPresenter.responseData(this,bean);
        }else {
            myTyjPresenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        myTyjPresenter.showError();
    }
}
