package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/9/20.
 */

public class ProductInvestConfirmBean extends BaseBean {

    //活期宝确认投资的bean
    /**
     * accountBalance : 899849
     * hbqRate : 7-11%
     * hqb : {"amount":3000000,"balance":2043619,"bdtime":null,"betime":1556163834000,"bstime":1461555834000,"code":"160425114354597605","cstime":null,"ctime":1461555834000,"exts":null,"id":39,"maxt":0,"mid":22,"mint":1,"nxtime":null,"period":1095,"rate":7,"rtype":5,"status":2,"summary":null,"title":"活期宝013","typeid":30,"zy":0}
     * mid : 2923
     */

    private Double accountBalance;
    private double canTenderAmount;


    private String hbqRate;
    private HqbBean hqb;
    private Integer mid;

    public double getCanTenderAmount() {
        return canTenderAmount;
    }

    public void setCanTenderAmount(double canTenderAmount) {
        this.canTenderAmount = canTenderAmount;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getHbqRate() {
        return hbqRate;
    }

    public void setHbqRate(String hbqRate) {
        this.hbqRate = hbqRate;
    }

    public HqbBean getHqb() {
        return hqb;
    }

    public void setHqb(HqbBean hqb) {
        this.hqb = hqb;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }


    //金芒果确认投资的bean

    private int couponNum;
    private Double hbAmount;
    private LoanBean loan;

    private List<ActivityGitBean> couponList;

    private List<ActivityGitBean> hbList;

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public Double getHbAmount() {
        return hbAmount;
    }

    public void setHbAmount(Double hbAmount) {
        this.hbAmount = hbAmount;
    }

    public LoanBean getLoan() {
        return loan;
    }

    public void setLoan(LoanBean loan) {
        this.loan = loan;
    }

    public List<ActivityGitBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<ActivityGitBean> couponList) {
        this.couponList = couponList;
    }

    public List<ActivityGitBean> getHbList() {
        return hbList;
    }

    public void setHbList(List<ActivityGitBean> hbList) {
        this.hbList = hbList;
    }


}
