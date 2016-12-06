package com.mgjr.model.listeners;

import com.mgjr.presenter.listeners.OnPresenterListener;

import java.util.Map;

/**
 * Created by wim on 16/8/16.
 */
public interface BaseModel {
    void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams, OnPresenterListener listener);
}
