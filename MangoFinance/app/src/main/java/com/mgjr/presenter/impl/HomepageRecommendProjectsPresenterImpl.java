package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.model.impl.HomepageRecommendProjectsModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomepageRecommendProjectsPresenterImpl implements OnPresenterListener<HomepageRecommendProjectsBean> {

    private ViewListener homepageRecommendProjectsViewLisenter;
    private BaseModel baseModel;

    public HomepageRecommendProjectsPresenterImpl(ViewListener homepageRecommendProjectsViewLisenter) {
        this.homepageRecommendProjectsViewLisenter = homepageRecommendProjectsViewLisenter;
        baseModel = new HomepageRecommendProjectsModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        homepageRecommendProjectsViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(HomepageRecommendProjectsBean bean) {
        homepageRecommendProjectsViewLisenter.hideLoading();
        if (bean != null) {
            if (bean.getStatus().equalsIgnoreCase("0000")) {
                homepageRecommendProjectsViewLisenter.responseData(this, bean);
            } else {
                homepageRecommendProjectsViewLisenter.showError(this, bean);
            }
        }
    }

    @Override
    public void onError() {
        homepageRecommendProjectsViewLisenter.showError();
    }
}
