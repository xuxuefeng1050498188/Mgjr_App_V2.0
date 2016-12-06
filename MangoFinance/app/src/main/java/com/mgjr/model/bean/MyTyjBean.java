package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MyTyjBean extends NewcomerTasteTenderBean {

    private Double ljlxNewcomer;

    private Double newcomerBalance;

    private Double newcomerTotalAmount;

    private List<InvestmentRecordBean> investmentRecord;

    public Double getLjlxNewcomer() {
        return ljlxNewcomer;
    }

    public void setLjlxNewcomer(Double ljlxNewcomer) {
        this.ljlxNewcomer = ljlxNewcomer;
    }

    public Double getNewcomerBalance() {
        return newcomerBalance;
    }

    public void setNewcomerBalance(Double newcomerBalance) {
        this.newcomerBalance = newcomerBalance;
    }

    public Double getNewcomerTotalAmount() {
        return newcomerTotalAmount;
    }

    public void setNewcomerTotalAmount(Double newcomerTotalAmount) {
        this.newcomerTotalAmount = newcomerTotalAmount;
    }

    public List<InvestmentRecordBean> getInvestmentRecord() {
        return investmentRecord;
    }

    public void setInvestmentRecord(List<InvestmentRecordBean> investmentRecord) {
        this.investmentRecord = investmentRecord;
    }

    public static class  InvestmentRecordBean{

        private Double amount;
        private long ctime;
        private int mid;
        private int fromsource;
        private int status;
        private String title;
        private Double profit;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getProfit() {
            return profit;
        }

        public void setProfit(Double profit) {
            this.profit = profit;
        }
    }

}
