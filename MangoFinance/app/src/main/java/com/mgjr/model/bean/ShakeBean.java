package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ShakeBean extends BaseBean {
    private String tips;
    private String days;
    private double yesterdayIncome;

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public double getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(double yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }
}
