package com.mgjr.model.bean.EventBusBean;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.bean.LoanBean;

/**
 * 活期宝投资完成返回的数据
 * Created by xuxuefeng on 2016/9/23.
 */

public class HqbInvestCompleteBean extends BaseBean {

    /**
     * amount : 10000
     * hqbTitle : 活期宝013
     * income1Year : 950
     * rate : 7-11%
     * time : 1474602399637
     */


    private int tenderCount;
    private Double amount;
    private String hqbTitle;
    private double income1Year;
    private String rate;
    private long time;
    private LoanBean recommendLoan;

    public LoanBean getRecommendLoan() {
        return recommendLoan;
    }

    public void setRecommendLoan(LoanBean recommendLoan) {
        this.recommendLoan = recommendLoan;
    }

    public int getTenderCount() {
        return tenderCount;
    }

    public void setTenderCount(int tenderCount) {
        this.tenderCount = tenderCount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getHqbTitle() {
        return hqbTitle;
    }

    public void setHqbTitle(String hqbTitle) {
        this.hqbTitle = hqbTitle;
    }

    public double getIncome1Year() {
        return income1Year;
    }

    public void setIncome1Year(double income1Year) {
        this.income1Year = income1Year;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
