package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyHqbBean;
import com.mgjr.model.impl.MyHqbModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */

public class MyHqbPresenterImpl implements OnPresenterListener<MyHqbBean> {
    private ViewListener myHqbPresenter;
    private BaseModel baseModel;

    public MyHqbPresenterImpl(ViewListener myHqbPresenter) {
        this.myHqbPresenter = myHqbPresenter;
        baseModel = new MyHqbModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        myHqbPresenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyHqbBean bean) {
        myHqbPresenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            myHqbPresenter.responseData(this,bean);
        }else {
            myHqbPresenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        myHqbPresenter.showError();
    }
}
