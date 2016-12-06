package com.mgjr.presenter.impl;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.impl.ResetLoginPwdModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ResetLoginPwdPresenterImpl implements OnPresenterListener<BaseBean> {

    private ViewListener resetLoginPwdViewListener;
    private BaseModel baseModel;

    public ResetLoginPwdPresenterImpl(ViewListener resetLoginPwdViewListener) {
        this.resetLoginPwdViewListener = resetLoginPwdViewListener;
        baseModel = new ResetLoginPwdModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        resetLoginPwdViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(BaseBean bean) {
        resetLoginPwdViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            resetLoginPwdViewListener.responseData(this,bean);
        }else {
            resetLoginPwdViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        resetLoginPwdViewListener.showError();
    }
}
