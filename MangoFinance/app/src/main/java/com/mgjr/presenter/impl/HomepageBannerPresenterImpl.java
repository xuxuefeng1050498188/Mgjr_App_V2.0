package com.mgjr.presenter.impl;

import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.model.impl.HomeBannerModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomepageBannerPresenterImpl implements OnPresenterListener<HomepageRecommendProjectsBean> {
    private ViewListener homepageBannerViewLisenter;
    private BaseModel baseModel;

    public HomepageBannerPresenterImpl(ViewListener homepageRecommendProjectsViewLisenter) {
        this.homepageBannerViewLisenter = homepageRecommendProjectsViewLisenter;
        baseModel = new HomeBannerModelImpl();
    }

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        homepageBannerViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);
    }


    @Override
    public void onSuccess(HomepageRecommendProjectsBean bean) {
        homepageBannerViewLisenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            homepageBannerViewLisenter.responseData(this,bean);
        }else {
            homepageBannerViewLisenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        homepageBannerViewLisenter.showError();
    }
}
