package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyHqbProjectDetailsBean;
import com.mgjr.model.impl.MyHqbProjectDetailsModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/30.
 */

public class MyHqbProjectDetailsPresenterImpl implements OnPresenterListener<MyHqbProjectDetailsBean> {

    private ViewListener myHqbProjectDetailsViewListener;
    private BaseModel baseModel;

    public MyHqbProjectDetailsPresenterImpl(ViewListener myHqbProjectDetailsViewListener) {
        this.myHqbProjectDetailsViewListener = myHqbProjectDetailsViewListener;
        baseModel = new MyHqbProjectDetailsModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        myHqbProjectDetailsViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyHqbProjectDetailsBean bean) {
        myHqbProjectDetailsViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            myHqbProjectDetailsViewListener.responseData(this,bean);
        }else {
            myHqbProjectDetailsViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        myHqbProjectDetailsViewListener.showError();
    }

}
