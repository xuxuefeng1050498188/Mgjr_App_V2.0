package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.model.impl.HomeEventsWindowModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomepageEventsWindowPresenterImpl implements OnPresenterListener<HomepageRecommendProjectsBean> {
    private ViewListener homepageEventsWindowViewLisenter;
    private BaseModel baseModel;

    public HomepageEventsWindowPresenterImpl(ViewListener homepageRecommendProjectsViewLisenter) {
        this.homepageEventsWindowViewLisenter = homepageRecommendProjectsViewLisenter;
        baseModel = new HomeEventsWindowModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        homepageEventsWindowViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(HomepageRecommendProjectsBean bean) {
        homepageEventsWindowViewLisenter.hideLoading();
        if (bean.getStatus() != null) {
            if (bean.getStatus().equalsIgnoreCase("0000")) {
                homepageEventsWindowViewLisenter.responseData(this, bean);
            } else {
                homepageEventsWindowViewLisenter.showError(this, bean);
            }
        }
    }

    @Override
    public void onError() {
        homepageEventsWindowViewLisenter.showError();
    }
}
