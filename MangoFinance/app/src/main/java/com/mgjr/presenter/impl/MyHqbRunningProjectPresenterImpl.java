package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyHqbRunningProjectBean;
import com.mgjr.model.impl.MyHqbRunningProjectModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */

public class MyHqbRunningProjectPresenterImpl implements OnPresenterListener<MyHqbRunningProjectBean> {
    private ViewListener myHqbRunningProjectPresenter;
    private BaseModel baseModel;

    public MyHqbRunningProjectPresenterImpl(ViewListener myHqbRunningProjectPresenter) {
        this.myHqbRunningProjectPresenter = myHqbRunningProjectPresenter;
        baseModel = new MyHqbRunningProjectModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        myHqbRunningProjectPresenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyHqbRunningProjectBean bean) {
        myHqbRunningProjectPresenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            myHqbRunningProjectPresenter.responseData(this,bean);
        }else {
            myHqbRunningProjectPresenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        myHqbRunningProjectPresenter.showError();
    }
}
