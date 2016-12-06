package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class HqbRedeemedBean extends BaseBean {

    private List<HqbTenderListBean> hqbTenderList;

    public List<HqbTenderListBean> getHqbTenderList() {
        return hqbTenderList;
    }

    public void setHqbTenderList(List<HqbTenderListBean> hqbTenderList) {
        this.hqbTenderList = hqbTenderList;
    }

    public static class HqbTenderListBean {
        private double amount;
        private String code;
        private long ctime;
        private int fromsource;
        private String hqbTitle;
        private int hqbid;
        private int id;
        private double incomeAmount;
        private long lastRedeemTime;
        private int mid;
        private double rate;
        private double redeemRate;
        private int redeemingCount;
        private double remainingTenderAmount;
        private int tenderDays;
        private String username;
        private double yesterdayIncome;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public String getHqbTitle() {
            return hqbTitle;
        }

        public void setHqbTitle(String hqbTitle) {
            this.hqbTitle = hqbTitle;
        }

        public int getHqbid() {
            return hqbid;
        }

        public void setHqbid(int hqbid) {
            this.hqbid = hqbid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(double incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public long getLastRedeemTime() {
            return lastRedeemTime;
        }

        public void setLastRedeemTime(long lastRedeemTime) {
            this.lastRedeemTime = lastRedeemTime;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getRedeemRate() {
            return redeemRate;
        }

        public void setRedeemRate(double redeemRate) {
            this.redeemRate = redeemRate;
        }

        public int getRedeemingCount() {
            return redeemingCount;
        }

        public void setRedeemingCount(int redeemingCount) {
            this.redeemingCount = redeemingCount;
        }

        public double getRemainingTenderAmount() {
            return remainingTenderAmount;
        }

        public void setRemainingTenderAmount(double remainingTenderAmount) {
            this.remainingTenderAmount = remainingTenderAmount;
        }

        public int getTenderDays() {
            return tenderDays;
        }

        public void setTenderDays(int tenderDays) {
            this.tenderDays = tenderDays;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getYesterdayIncome() {
            return yesterdayIncome;
        }

        public void setYesterdayIncome(double yesterdayIncome) {
            this.yesterdayIncome = yesterdayIncome;
        }
    }
}
