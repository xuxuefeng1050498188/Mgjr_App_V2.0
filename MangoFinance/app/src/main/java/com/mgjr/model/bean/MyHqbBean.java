package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/9/21.
 */

public class MyHqbBean extends BaseBean {

    private double totalTenderAl;
    private double totalTender;
    private double dsbjHqb;
    private double hbqYesterdayIncome;
    private double redeemingAmount;
    private String tipsMessage;
    private double yslxHqb;
    private String hbqCode;
    private String showMessage;
    private int tenderCount;

    public int getTenderCount() {
        return tenderCount;
    }

    public void setTenderCount(int tenderCount) {
        this.tenderCount = tenderCount;
    }

    public double getTotalTenderAl() {
        return totalTenderAl;
    }

    public void setTotalTenderAl(double totalTenderAl) {
        this.totalTenderAl = totalTenderAl;
    }

    public double getTotalTender() {
        return totalTender;
    }

    public void setTotalTender(double totalTender) {
        this.totalTender = totalTender;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public String getHbqCode() {
        return hbqCode;
    }

    public void setHbqCode(String hbqCode) {
        this.hbqCode = hbqCode;
    }

    public double getDsbjHqb() {
        return dsbjHqb;
    }

    public void setDsbjHqb(double dsbjHqb) {
        this.dsbjHqb = dsbjHqb;
    }

    public double getHbqYesterdayIncome() {
        return hbqYesterdayIncome;
    }

    public void setHbqYesterdayIncome(double hbqYesterdayIncome) {
        this.hbqYesterdayIncome = hbqYesterdayIncome;
    }

    public double getRedeemingAmount() {
        return redeemingAmount;
    }

    public void setRedeemingAmount(double redeemingAmount) {
        this.redeemingAmount = redeemingAmount;
    }

    public String getTipsMessage() {
        return tipsMessage;
    }

    public void setTipsMessage(String tipsMessage) {
        this.tipsMessage = tipsMessage;
    }

    public double getYslxHqb() {
        return yslxHqb;
    }

    public void setYslxHqb(double yslxHqb) {
        this.yslxHqb = yslxHqb;
    }
}
