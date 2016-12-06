package com.mgjr.presenter.impl;

import com.mgjr.model.bean.ShakeBean;
import com.mgjr.model.impl.ShakeGetIncomeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ShakeGetYestodayIncomePresenterImpl implements OnPresenterListener<ShakeBean> {

    private ViewListener shakeGetYestodayIncomeViewListener;
    private BaseModel baseModel;

    public ShakeGetYestodayIncomePresenterImpl(ViewListener shakeGetYestodayIncomeViewListener) {
        this.shakeGetYestodayIncomeViewListener = shakeGetYestodayIncomeViewListener;
        baseModel = new ShakeGetIncomeModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        shakeGetYestodayIncomeViewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }

    @Override
    public void onSuccess(ShakeBean bean) {
        shakeGetYestodayIncomeViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            shakeGetYestodayIncomeViewListener.responseData(this,bean);
        }else {
            shakeGetYestodayIncomeViewListener.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        shakeGetYestodayIncomeViewListener.showError();
    }
}
