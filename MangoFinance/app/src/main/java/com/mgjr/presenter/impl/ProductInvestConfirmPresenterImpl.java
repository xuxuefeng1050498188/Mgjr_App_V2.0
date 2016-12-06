package com.mgjr.presenter.impl;

import com.mgjr.model.bean.ProductInvestConfirmBean;
import com.mgjr.model.impl.InvestProductDetailModelImpl;
import com.mgjr.model.impl.ProductInvestConfirmModelImpl;
import com.mgjr.model.listeners.BaseModel;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.view.listeners.ViewListener;

import java.util.Map;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class ProductInvestConfirmPresenterImpl implements OnPresenterListener<ProductInvestConfirmBean> {

    private ViewListener<ProductInvestConfirmBean> viewListener;
    private BaseModel baseModel;

    public ProductInvestConfirmPresenterImpl(ViewListener<ProductInvestConfirmBean> viewListener) {
        this.viewListener = viewListener;
        baseModel = new ProductInvestConfirmModelImpl();
    }

    @Override
    public void onSuccess(ProductInvestConfirmBean bean) {
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

    public void sendRequest(Map<String, String> necessaryParams, Map<String, String> unNecessaryParams) {
        viewListener.showLoading();
        baseModel.sendRequest(necessaryParams, unNecessaryParams, this);

    }
}
