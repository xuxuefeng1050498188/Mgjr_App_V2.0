package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/9/23.
 */

public class MyJmgFinishedProjectsBean extends BaseBean {

    /**
     * amount : 8000
     * code : 160715170653302567
     * coupon : null
     * ctime : 1468573613000
     * experienceAmount : null
     * expireTime : 1469585845000
     * extraAmount : null
     * fromsource : 1
     * gifts : null
     * id : 8368
     * loanTitle : 金芒果一期222
     * loanid : 439
     * loanstatus : 200
     * message : null
     * mid : 741
     * nextRepayTime : null
     * period : 3
     * preExpireTime : null
     * preIntefee : null
     * rate : 10.5
     * repayCount : 3
     * repayIntefee : null
     * status : 2
     * type : 0
     * username : null
     * zy : 1.5
     */

    private List<OverTenderListBean> overTenderList;

    public List<OverTenderListBean> getOverTenderList() {
        return overTenderList;
    }

    public void setOverTenderList(List<OverTenderListBean> overTenderList) {
        this.overTenderList = overTenderList;
    }

    public static class OverTenderListBean {
        private double amount;
        private String code;
        private Object coupon;
        private long ctime;
        private Object experienceAmount;
        private long expireTime;
        private Object extraAmount;
        private int fromsource;
        private Object gifts;
        private int id;
        private String loanTitle;
        private int loanid;
        private int loanstatus;
        private Object message;
        private int mid;
        private Object nextRepayTime;
        private int period;
        private Object preExpireTime;
        private Object preIntefee;
        private double rate;
        private int repayCount;
        private Object repayIntefee;
        private int status;
        private int type;
        private Object username;
        private double zy;

        private long intefeeStartTime;

        public long getIntefeeStartTime() {
            return intefeeStartTime;
        }

        public void setIntefeeStartTime(long intefeeStartTime) {
            this.intefeeStartTime = intefeeStartTime;
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

        public Object getCoupon() {
            return coupon;
        }

        public void setCoupon(Object coupon) {
            this.coupon = coupon;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public Object getExperienceAmount() {
            return experienceAmount;
        }

        public void setExperienceAmount(Object experienceAmount) {
            this.experienceAmount = experienceAmount;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public Object getExtraAmount() {
            return extraAmount;
        }

        public void setExtraAmount(Object extraAmount) {
            this.extraAmount = extraAmount;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public Object getGifts() {
            return gifts;
        }

        public void setGifts(Object gifts) {
            this.gifts = gifts;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLoanTitle() {
            return loanTitle;
        }

        public void setLoanTitle(String loanTitle) {
            this.loanTitle = loanTitle;
        }

        public int getLoanid() {
            return loanid;
        }

        public void setLoanid(int loanid) {
            this.loanid = loanid;
        }

        public int getLoanstatus() {
            return loanstatus;
        }

        public void setLoanstatus(int loanstatus) {
            this.loanstatus = loanstatus;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public Object getNextRepayTime() {
            return nextRepayTime;
        }

        public void setNextRepayTime(Object nextRepayTime) {
            this.nextRepayTime = nextRepayTime;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public Object getPreExpireTime() {
            return preExpireTime;
        }

        public void setPreExpireTime(Object preExpireTime) {
            this.preExpireTime = preExpireTime;
        }

        public Object getPreIntefee() {
            return preIntefee;
        }

        public void setPreIntefee(Object preIntefee) {
            this.preIntefee = preIntefee;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getRepayCount() {
            return repayCount;
        }

        public void setRepayCount(int repayCount) {
            this.repayCount = repayCount;
        }

        public Object getRepayIntefee() {
            return repayIntefee;
        }

        public void setRepayIntefee(Object repayIntefee) {
            this.repayIntefee = repayIntefee;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public double getZy() {
            return zy;
        }

        public void setZy(double zy) {
            this.zy = zy;
        }
    }
}
