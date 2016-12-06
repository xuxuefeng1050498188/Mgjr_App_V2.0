package com.mgjr.presenter.impl;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.impl.GetForgetLoginPwdSmsCodeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class GetForgetLoginPwdSmsCodePresenterImpl implements OnPresenterListener<BaseBean> {

    private ViewListener forgetLoginPwdViewlistener;
    private BaseModel baseModel;

    public GetForgetLoginPwdSmsCodePresenterImpl(ViewListener forgetLoginPwdViewlistener) {
        this.forgetLoginPwdViewlistener = forgetLoginPwdViewlistener;
        baseModel = new GetForgetLoginPwdSmsCodeModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        forgetLoginPwdViewlistener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(BaseBean bean) {
        forgetLoginPwdViewlistener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            forgetLoginPwdViewlistener.responseData(this,bean);
        }else {
            forgetLoginPwdViewlistener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        forgetLoginPwdViewlistener.showError();
    }
}
