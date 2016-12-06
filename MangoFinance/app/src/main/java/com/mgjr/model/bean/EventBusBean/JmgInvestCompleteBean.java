package com.mgjr.model.bean.EventBusBean;

import com.mgjr.model.bean.BaseBean;
import com.mgjr.model.bean.HqbBean;
import com.mgjr.model.bean.LoanBean;

/**
 * 金芒果投资完成返回的数据
 * Created by xuxuefeng on 2016/9/23.
 */

public class JmgInvestCompleteBean extends BaseBean {

    private double amount;
    private double hbAmount;
    private int period;
    private String title;
    private HqbBean hqb;
    private LoanBean jmg;
    private long preExpireTime;
    private double preIncome;
    private double rate;
    private long time;
    private int tenderCount;

    public double getHbAmount() {
        return hbAmount;
    }

    public void setHbAmount(double hbAmount) {
        this.hbAmount = hbAmount;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public LoanBean getJmg() {
        return jmg;
    }

    public void setJmg(LoanBean jmg) {
        this.jmg = jmg;
    }

    public int getTenderCount() {
        return tenderCount;
    }

    public void setTenderCount(int tenderCount) {
        this.tenderCount = tenderCount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public HqbBean getHqb() {
        return hqb;
    }

    public void setHqb(HqbBean hqb) {
        this.hqb = hqb;
    }

    public long getPreExpireTime() {
        return preExpireTime;
    }

    public void setPreExpireTime(long preExpireTime) {
        this.preExpireTime = preExpireTime;
    }

    public double getPreIncome() {
        return preIncome;
    }

    public void setPreIncome(double preIncome) {
        this.preIncome = preIncome;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
