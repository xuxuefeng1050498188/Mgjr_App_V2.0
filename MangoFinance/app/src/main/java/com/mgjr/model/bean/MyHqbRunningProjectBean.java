package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class MyHqbRunningProjectBean extends BaseBean {



    private List<TenderListBean> tenderList;


    private List<HqbTenderListBean> hqbTenderList;

    public List<TenderListBean> getTenderList() {
        return tenderList;
    }

    public void setTenderList(List<TenderListBean> tenderList) {
        this.tenderList = tenderList;
    }

    public List<HqbTenderListBean> getHqbTenderList() {
        return hqbTenderList;
    }

    public void setHqbTenderList(List<HqbTenderListBean> hqbTenderList) {
        this.hqbTenderList = hqbTenderList;
    }



    public static class TenderListBean {
        private boolean isVisiable;
        public boolean isChecked;
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

        public boolean isVisiable() {
            return isVisiable;
        }

        public void setVisiable(boolean visiable) {
            isVisiable = visiable;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

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

    public static class HqbTenderListBean {
        private int amount;
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
        private int remainingTenderAmount;
        private int tenderDays;
        private String username;
        private double yesterdayIncome;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
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

        public int getRemainingTenderAmount() {
            return remainingTenderAmount;
        }

        public void setRemainingTenderAmount(int remainingTenderAmount) {
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
