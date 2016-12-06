package com.mgjr.presenter.impl;

import com.mgjr.model.bean.APPVersion;
import com.mgjr.model.bean.NoiceBean;
import com.mgjr.model.impl.APPUpdateModelImpl;
import com.mgjr.model.impl.NoticeModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by wim on 16/10/25.
 */

public class NoticePresenterImpl implements OnPresenterListener<NoiceBean> {
    private ViewListener<NoiceBean> viewListener;
    private BaseModel baseModel;

    public NoticePresenterImpl(ViewListener<NoiceBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new NoticeModelImpl();
    }

    public void sendRequest() {
        viewListener.showLoading();
        baseModel.sendRequest(null, null, this);

    }

    @Override
    public void onSuccess(NoiceBean bean) {
        viewListener.hideLoading();
        if (bean.getStatus().equalsIgnoreCase("0000")) {
            viewListener.responseData(this, bean);
        } else {
            viewListener.showError(this, bean);
        }

    }

    @Override
    public void onError() {
        viewListener.showError();
    }
}
