package com.mgjr.model.bean.EventBusBean;

import com.mgjr.model.bean.BaseBean;

/**
 * Created by Administrator on 2016/9/23.
 */

public class HqbMutilRedeemBean extends BaseBean {


    /**
     * incometotal : 0
     * incometotal30 : 0
     * rateMax : 7
     * redeemAmountTotal : 210
     * tips : 活期宝013等，共3个项目
     */

    private double incometotal;
    private double incometotal30;
    private double rateMax;
    private double redeemAmountTotal;
    private String tips;

    public double getRedeemAmountTotal() {
        return redeemAmountTotal;
    }

    public void setRedeemAmountTotal(double redeemAmountTotal) {
        this.redeemAmountTotal = redeemAmountTotal;
    }

    public double getRateMax() {
        return rateMax;
    }

    public void setRateMax(double rateMax) {
        this.rateMax = rateMax;
    }

    public double getIncometotal30() {
        return incometotal30;
    }

    public void setIncometotal30(double incometotal30) {
        this.incometotal30 = incometotal30;
    }

    public double getIncometotal() {
        return incometotal;
    }

    public void setIncometotal(double incometotal) {
        this.incometotal = incometotal;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
