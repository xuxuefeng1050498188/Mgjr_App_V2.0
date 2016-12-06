package com.mgjr.model.bean;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/26.
 */
public class UserCenterBean extends BaseBean {
    private String headImgUrl;

    private String nickname;

    private Map actionMap ;

    public Map getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map actionMap) {
        this.actionMap = actionMap;
    }

//    public static class ActionMap {
//        private Map<String, String> actions;
//
//        public Map<String, String> getActions() {
//            return actions;
//        }
//
//        public void setActions(Map<String, String> actions) {
//            this.actions = actions;
//        }
//    }

//    public ActionMap getActionMap() {
//        return actionMap;
//    }
//
//    public void setActionMap(ActionMap actionMap) {
//        this.actionMap = actionMap;
//    }

    private Member member;

    /**
     * 10 : 充值
     * 1000,1020 : 活动奖励
     * 106 : 理财师收益
     * 107 : 体验金收益
     * 20,21 : 提现
     * 200 : 活期宝收益
     * 202 : 活期宝转入
     * 204 : 活期宝赎回
     * 50 : 金芒果还本
     * 51 : 金芒果收益
     * 80,90 : 金芒果投资
     */


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    private double accountBalance;

    private int couponNum;

    private double dsbjHqb;

    private double dsbjLoan;

    private double financialProfit;

    private double hbAmount;

    private double redeemingjHqb;

    private double totalAssets;

    private double totalIncome;

    private double xsbAmount;

    private double yesterdayIncome;

    private double yslxHqb;

    private double yslxLoan;

    private double yslxXsb;

    private double frozenTenderAmount;

    private double withdrawingAmount;

    private double activityReward;

    private double yslxMgb;

    private double dsbjMgb;

    public double getDsbjMgb() {
        return dsbjMgb;
    }

    public void setDsbjMgb(double dsbjMgb) {
        this.dsbjMgb = dsbjMgb;
    }

    public double getYslxMgb() {
        return yslxMgb;
    }

    public void setYslxMgb(double yslxMgb) {
        this.yslxMgb = yslxMgb;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setDsbjHqb(double dsbjHqb) {
        this.dsbjHqb = dsbjHqb;
    }

    public void setDsbjLoan(double dsbjLoan) {
        this.dsbjLoan = dsbjLoan;
    }

    public void setFinancialProfit(double financialProfit) {
        this.financialProfit = financialProfit;
    }

    public void setHbAmount(double hbAmount) {
        this.hbAmount = hbAmount;
    }

    public void setRedeemingjHqb(double redeemingjHqb) {
        this.redeemingjHqb = redeemingjHqb;
    }

    public void setTotalAssets(double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public void setXsbAmount(double xsbAmount) {
        this.xsbAmount = xsbAmount;
    }

    public void setYesterdayIncome(double yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public void setYslxHqb(double yslxHqb) {
        this.yslxHqb = yslxHqb;
    }

    public void setYslxLoan(double yslxLoan) {
        this.yslxLoan = yslxLoan;
    }

    public void setYslxXsb(double yslxXsb) {
        this.yslxXsb = yslxXsb;
    }

    public double getFrozenTenderAmount() {
        return frozenTenderAmount;
    }

    public void setFrozenTenderAmount(double frozenTenderAmount) {
        this.frozenTenderAmount = frozenTenderAmount;
    }

    public double getWithdrawingAmount() {
        return withdrawingAmount;
    }

    public void setWithdrawingAmount(double withdrawingAmount) {
        this.withdrawingAmount = withdrawingAmount;
    }

    public double getActivityReward() {
        return activityReward;
    }

    public void setActivityReward(double activityReward) {
        this.activityReward = activityReward;
    }

    public static class Member {

        private Long becomepttime;

        private Long ctime;

        private int ctype;

        private String email;

        private int eok;

        private int fromsource;

        private int id;

        private String idcode;

        private int idtype;

        private int lcs;

        private String logip;

        private String logpwd;

        private Long logtime;

        private int membertype;

        private String mobile;

        private int mok;

        private String paypwd;

        private int pmid;

        private String roles;

        private int status;

        private String truename;

        private String username;

        private int vip;


        public int getCtype() {
            return ctype;
        }

        public void setCtype(int ctype) {
            this.ctype = ctype;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getEok() {
            return eok;
        }

        public void setEok(int eok) {
            this.eok = eok;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdcode() {
            return idcode;
        }

        public void setIdcode(String idcode) {
            this.idcode = idcode;
        }

        public int getIdtype() {
            return idtype;
        }

        public void setIdtype(int idtype) {
            this.idtype = idtype;
        }

        public int getLcs() {
            return lcs;
        }

        public void setLcs(int lcs) {
            this.lcs = lcs;
        }

        public String getLogip() {
            return logip;
        }

        public void setLogip(String logip) {
            this.logip = logip;
        }

        public String getLogpwd() {
            return logpwd;
        }

        public void setLogpwd(String logpwd) {
            this.logpwd = logpwd;
        }

        public int getMembertype() {
            return membertype;
        }

        public Long getBecomepttime() {
            return becomepttime;
        }

        public void setBecomepttime(Long becomepttime) {
            this.becomepttime = becomepttime;
        }

        public Long getCtime() {
            return ctime;
        }

        public void setCtime(Long ctime) {
            this.ctime = ctime;
        }

        public Long getLogtime() {
            return logtime;
        }

        public void setLogtime(Long logtime) {
            this.logtime = logtime;
        }

        public void setMembertype(int membertype) {
            this.membertype = membertype;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMok() {
            return mok;
        }

        public void setMok(int mok) {
            this.mok = mok;
        }

        public String getPaypwd() {
            return paypwd;
        }

        public void setPaypwd(String paypwd) {
            this.paypwd = paypwd;
        }

        public int getPmid() {
            return pmid;
        }

        public void setPmid(int pmid) {
            this.pmid = pmid;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public Double getDsbjHqb() {
        return dsbjHqb;
    }

    public void setDsbjHqb(Double dsbjHqb) {
        this.dsbjHqb = dsbjHqb;
    }

    public Double getDsbjLoan() {
        return dsbjLoan;
    }

    public void setDsbjLoan(Double dsbjLoan) {
        this.dsbjLoan = dsbjLoan;
    }

    public Double getFinancialProfit() {
        return financialProfit;
    }

    public void setFinancialProfit(Double financialProfit) {
        this.financialProfit = financialProfit;
    }

    public Double getHbAmount() {
        return hbAmount;
    }

    public void setHbAmount(Double hbAmount) {
        this.hbAmount = hbAmount;
    }

    public Double getRedeemingjHqb() {
        return redeemingjHqb;
    }

    public void setRedeemingjHqb(Double redeemingjHqb) {
        this.redeemingjHqb = redeemingjHqb;
    }

    public Double getXsbAmount() {
        return xsbAmount;
    }

    public void setXsbAmount(Double xsbAmount) {
        this.xsbAmount = xsbAmount;
    }

    public Double getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(Double yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }


    public Double getYslxHqb() {
        return yslxHqb;
    }

    public void setYslxHqb(Double yslxHqb) {
        this.yslxHqb = yslxHqb;
    }

    public Double getYslxLoan() {
        return yslxLoan;
    }

    public void setYslxLoan(Double yslxLoan) {
        this.yslxLoan = yslxLoan;
    }

    public Double getYslxXsb() {
        return yslxXsb;
    }

    public void setYslxXsb(Double yslxXsb) {
        this.yslxXsb = yslxXsb;
    }
}
