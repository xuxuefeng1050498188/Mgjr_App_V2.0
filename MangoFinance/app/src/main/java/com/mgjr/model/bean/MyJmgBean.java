package com.mgjr.model.bean;

/**
 * Created by xuxuefeng on 2016/9/8.
 */
public class MyJmgBean extends BaseBean {


    private double dsbjLoan;
    private double dslxLoan;
    private double frozenTenderAmount;
    private double jmgYesterdayIncome;
    private String tipsMessage;
    private double yslxLoan;
    private double totalTenderAmount;
    /**
     * tenderCount : 55
     */

    private int tenderCount;

    public double getTotalTenderAmount() {
        return totalTenderAmount;
    }

    public void setTotalTenderAmount(double totalTenderAmount) {
        this.totalTenderAmount = totalTenderAmount;
    }

    public double getDsbjLoan() {
        return dsbjLoan;
    }

    public void setDsbjLoan(double dsbjLoan) {
        this.dsbjLoan = dsbjLoan;
    }

    public double getDslxLoan() {
        return dslxLoan;
    }

    public void setDslxLoan(double dslxLoan) {
        this.dslxLoan = dslxLoan;
    }

    public double getFrozenTenderAmount() {
        return frozenTenderAmount;
    }

    public void setFrozenTenderAmount(double frozenTenderAmount) {
        this.frozenTenderAmount = frozenTenderAmount;
    }

    public double getJmgYesterdayIncome() {
        return jmgYesterdayIncome;
    }

    public void setJmgYesterdayIncome(double jmgYesterdayIncome) {
        this.jmgYesterdayIncome = jmgYesterdayIncome;
    }

    public String getTipsMessage() {
        return tipsMessage;
    }

    public void setTipsMessage(String tipsMessage) {
        this.tipsMessage = tipsMessage;
    }

    public double getYslxLoan() {
        return yslxLoan;
    }

    public void setYslxLoan(double yslxLoan) {
        this.yslxLoan = yslxLoan;
    }

    public int getTenderCount() {
        return tenderCount;
    }

    public void setTenderCount(int tenderCount) {
        this.tenderCount = tenderCount;
    }
}
