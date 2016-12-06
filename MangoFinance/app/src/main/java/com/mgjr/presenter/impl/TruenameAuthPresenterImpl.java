package com.mgjr.presenter.impl;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.impl.TruenameAuthModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/29.
 */
public class TruenameAuthPresenterImpl implements OnPresenterListener<BaseBean> {

    private ViewListener truenameAuthPresenter;
    private BaseModel baseModel;

    public TruenameAuthPresenterImpl(ViewListener truenameAuthPresenter) {
        this.truenameAuthPresenter = truenameAuthPresenter;
        baseModel = new TruenameAuthModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        truenameAuthPresenter.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(BaseBean bean) {
        truenameAuthPresenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            truenameAuthPresenter.responseData(this,bean);
        }else {
            truenameAuthPresenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        truenameAuthPresenter.showError();
    }
}
