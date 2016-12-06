package com.mgjr.presenter.impl;

import com.mgjr.model.bean.FriendListBean;
import com.mgjr.model.impl.FriendListModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FriendListPresenterImpl implements OnPresenterListener<FriendListBean> {
    private ViewListener FriendListViewListener;

    private BaseModel baseModel;

    public FriendListPresenterImpl(ViewListener FriendListViewListener) {
        this.FriendListViewListener = FriendListViewListener;
        baseModel = new FriendListModelImpl();
    }



    public void sendRequest (Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
        FriendListViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(FriendListBean bean) {
        FriendListViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            FriendListViewListener.responseData(this,bean);
        }else {
            FriendListViewListener.showError(this,bean);
        }
    }


    @Override
    public void onError() {
        FriendListViewListener.showError();
    }

}
