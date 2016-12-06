package com.mgjr.view.listeners;

import com.mgjr.presenter.listeners.OnPresenterListener;

/**
 * Created by wim on 16/8/17.
 */
public interface ViewListener <T>{
    void showLoading();
    void hideLoading();
    void showError();
    void showError(OnPresenterListener listener,T t);
    void responseData(OnPresenterListener listener, T t);
}
