package com.mgjr.presenter.impl;

import com.mgjr.model.bean.MyHqbRunningProjectBean;
import com.mgjr.model.impl.HqbMutilRedeemListModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */

public class HqbMutilRedeemListPresenterImpl implements OnPresenterListener<MyHqbRunningProjectBean> {
    private ViewListener HqbMutilRedeemViewLisenter;
    private BaseModel baseModel;

    public HqbMutilRedeemListPresenterImpl(ViewListener HqbMutilRedeemViewLisenter) {
        this.HqbMutilRedeemViewLisenter = HqbMutilRedeemViewLisenter;
        baseModel = new HqbMutilRedeemListModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams){
        HqbMutilRedeemViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(MyHqbRunningProjectBean bean) {
        HqbMutilRedeemViewLisenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            HqbMutilRedeemViewLisenter.responseData(this,bean);
        }else {
            HqbMutilRedeemViewLisenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        HqbMutilRedeemViewLisenter.showError();
    }
}
