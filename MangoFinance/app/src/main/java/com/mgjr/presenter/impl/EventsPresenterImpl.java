package com.mgjr.presenter.impl;

import com.mgjr.model.bean.EventsBean;
import com.mgjr.model.impl.EventsModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public class EventsPresenterImpl implements OnPresenterListener<EventsBean> {

    private ViewListener eventsPresenterViewLisenter;

    private BaseModel baseModel;

    public EventsPresenterImpl(ViewListener eventsPresenterViewLisenter) {
        this.eventsPresenterViewLisenter = eventsPresenterViewLisenter;
        baseModel = new EventsModelImpl();
    }



    public void sendRequest (Map<String,String> necessaryParams,Map<String,String> unNecessaryParams){
        eventsPresenterViewLisenter.showLoading();
        baseModel.sendRequest(necessaryParams,unNecessaryParams,this);
    }


    @Override
    public void onSuccess(EventsBean bean) {
        eventsPresenterViewLisenter.hideLoading();
        if(bean.getStatus().equalsIgnoreCase("0000")){
            eventsPresenterViewLisenter.responseData(this,bean);
        }else {
            eventsPresenterViewLisenter.showError(this,bean);
        }
    }

    @Override
    public void onError() {
        eventsPresenterViewLisenter.showError();
    }
}
