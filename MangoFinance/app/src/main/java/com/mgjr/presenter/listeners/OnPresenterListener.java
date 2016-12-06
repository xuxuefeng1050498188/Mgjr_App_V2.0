package com.mgjr.presenter.listeners;

/**
 * Created by wim on 16/8/16.
 */
public interface OnPresenterListener<T>{
    void onSuccess(T bean);
    void onError();
}
