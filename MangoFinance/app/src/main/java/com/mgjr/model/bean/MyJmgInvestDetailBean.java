package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/9/23.
 */

public class MyJmgInvestDetailBean extends BaseBean {


    private double hbAmount;
    private double coupons;

    public double getCoupons() {
        return coupons;
    }

    public void setCoupons(double coupons) {
        this.coupons = coupons;
    }


    private TenderDetailBean tenderDetail;

    private List<RepaymentPlanBean> repaymentPlanList;

    public double getHbAmount() {
        return hbAmount;
    }

    public void setHbAmount(double hbAmount) {
        this.hbAmount = hbAmount;
    }

    public TenderDetailBean getTenderDetail() {
        return tenderDetail;
    }

    public void setTenderDetail(TenderDetailBean tenderDetail) {
        this.tenderDetail = tenderDetail;
    }

    public List<RepaymentPlanBean> getRepaymentPlanList() {
        return repaymentPlanList;
    }

    public void setRepaymentPlanList(List<RepaymentPlanBean> repaymentPlanList) {
        this.repaymentPlanList = repaymentPlanList;
    }

    public static class TenderDetailBean {
        private double amount;
        private String code;
        private double coupon;
        private long ctime;
        private double experienceAmount;
        private long expireTime;
        private double extraAmount;
        private int fromsource;
        private Object gifts;
        private int id;
        private String loanTitle;
        private int loanid;
        private int loanstatus;
        private String message;
        private int mid;
        private long nextRepayTime;
        private int period;
        private long preExpireTime;
        private double preIntefee;
        private double rate;
        private int repayCount;
        private double repayIntefee;
        private int status;
        private int type;
        private String username;
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

        public double getCoupon() {
            return coupon;
        }

        public void setCoupon(double coupon) {
            this.coupon = coupon;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public double getExperienceAmount() {
            return experienceAmount;
        }

        public void setExperienceAmount(double experienceAmount) {
            this.experienceAmount = experienceAmount;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public double getExtraAmount() {
            return extraAmount;
        }

        public void setExtraAmount(double extraAmount) {
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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public long getNextRepayTime() {
            return nextRepayTime;
        }

        public void setNextRepayTime(long nextRepayTime) {
            this.nextRepayTime = nextRepayTime;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public long getPreExpireTime() {
            return preExpireTime;
        }

        public void setPreExpireTime(long preExpireTime) {
            this.preExpireTime = preExpireTime;
        }

        public double getPreIntefee() {
            return preIntefee;
        }

        public void setPreIntefee(double preIntefee) {
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

        public double getRepayIntefee() {
            return repayIntefee;
        }

        public void setRepayIntefee(double repayIntefee) {
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getZy() {
            return zy;
        }

        public void setZy(double zy) {
            this.zy = zy;
        }
    }

    public static class RepaymentPlanBean {
        private double amount;
        private long cime;
        private String loanTitle;
        private String no;
        private int period;
        private long preRepayTime;
        private long repayTime;
        private int status;
        private String type;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getCime() {
            return cime;
        }

        public void setCime(long cime) {
            this.cime = cime;
        }

        public String getLoanTitle() {
            return loanTitle;
        }

        public void setLoanTitle(String loanTitle) {
            this.loanTitle = loanTitle;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public long getPreRepayTime() {
            return preRepayTime;
        }

        public void setPreRepayTime(long preRepayTime) {
            this.preRepayTime = preRepayTime;
        }

        public long getRepayTime() {
            return repayTime;
        }

        public void setRepayTime(long repayTime) {
            this.repayTime = repayTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
