package com.mgjr.presenter.impl;

import com.mgjr.model.bean.FinancialPlannerBean;
import com.mgjr.model.impl.FinancialPlannerModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FinancialPlannerPresenterImpl implements OnPresenterListener<FinancialPlannerBean> {

    private ViewListener FinancialPlannerViewListener;

    private BaseModel baseModel;

    public FinancialPlannerPresenterImpl(ViewListener FinancialPlannerViewListener) {
        this.FinancialPlannerViewListener = FinancialPlannerViewListener;
        baseModel = new FinancialPlannerModelImpl();
    }



    public void sendRequest (Map<String,String> necessaryParams, Map<String,String> unNecessaryParams){
        FinancialPlannerViewListener.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(FinancialPlannerBean bean) {
        FinancialPlannerViewListener.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            FinancialPlannerViewListener.responseData(this,bean);
        }else {
            FinancialPlannerViewListener.showError(this,bean);
        }
    }


    @Override
    public void onError() {
        FinancialPlannerViewListener.showError();
    }
}
