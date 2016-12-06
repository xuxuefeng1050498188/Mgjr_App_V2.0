package com.mgjr.model.bean.EventBusBean;

import com.mgjr.model.bean.MangoBoxBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MangoBoxDataBean {

    //可用加息券列表
    private List<MangoBoxBean.CanUsedCouponListBean> canUsedCouponList;
    //过期加息券列表
    private List<MangoBoxBean.OverCouponListBean> overCouponList;
    //已使用加息券
    private List<MangoBoxBean.UsedCouponListBean> usedCouponList;

    //可用红包列表
    private List<MangoBoxBean.CanUsedtHBListBean> canUsedtHBList;
    //过期红包列表
    private List<MangoBoxBean.OverHBListBean> overHBList;
    //已使用红包
    private List<MangoBoxBean.UsedtHBListBean> usedtHBList;

    private MangoBoxBean.GiftMapBean giftMapBean;

    public MangoBoxBean.GiftMapBean getGiftMapBean() {
        return giftMapBean;
    }

    public void setGiftMapBean(MangoBoxBean.GiftMapBean giftMapBean) {
        this.giftMapBean = giftMapBean;
    }

    public List<MangoBoxBean.CanUsedCouponListBean> getCanUsedCouponList() {
        return canUsedCouponList;
    }

    public void setCanUsedCouponList(List<MangoBoxBean.CanUsedCouponListBean> canUsedCouponList) {
        this.canUsedCouponList = canUsedCouponList;
    }

    public List<MangoBoxBean.OverCouponListBean> getOverCouponList() {
        return overCouponList;
    }

    public void setOverCouponList(List<MangoBoxBean.OverCouponListBean> overCouponList) {
        this.overCouponList = overCouponList;
    }

    public List<MangoBoxBean.UsedCouponListBean> getUsedCouponList() {
        return usedCouponList;
    }

    public void setUsedCouponList(List<MangoBoxBean.UsedCouponListBean> usedCouponList) {
        this.usedCouponList = usedCouponList;
    }

    public List<MangoBoxBean.CanUsedtHBListBean> getCanUsedtHBList() {
        return canUsedtHBList;
    }

    public void setCanUsedtHBList(List<MangoBoxBean.CanUsedtHBListBean> canUsedtHBList) {
        this.canUsedtHBList = canUsedtHBList;
    }

    public List<MangoBoxBean.OverHBListBean> getOverHBList() {
        return overHBList;
    }

    public void setOverHBList(List<MangoBoxBean.OverHBListBean> overHBList) {
        this.overHBList = overHBList;
    }

    public List<MangoBoxBean.UsedtHBListBean> getUsedtHBList() {
        return usedtHBList;
    }

    public void setUsedtHBList(List<MangoBoxBean.UsedtHBListBean> usedtHBList) {
        this.usedtHBList = usedtHBList;
    }
}
