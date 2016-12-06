package com.mgjr.presenter.impl;

import com.mgjr.model.bean.GetVcodeBean;
import com.mgjr.model.impl.GetVcodeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/25.
 */
public class GetVcodePresenterImpl implements OnPresenterListener<GetVcodeBean> {

    private ViewListener getVcodeViewListener;
    private BaseModel baseModel;

    public GetVcodePresenterImpl(ViewListener getVcodeViewListener) {
        this.getVcodeViewListener = getVcodeViewListener;
        baseModel = new GetVcodeModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        getVcodeViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(GetVcodeBean bean) {
        getVcodeViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            getVcodeViewListener.responseData(this,bean);
        }else {
            getVcodeViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
            getVcodeViewListener.showError();
    }
}
