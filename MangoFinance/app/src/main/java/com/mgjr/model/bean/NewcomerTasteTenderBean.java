package com.mgjr.model.bean;

/**
 * Created by Administrator on 2016/9/1.
 */
public class NewcomerTasteTenderBean extends BaseBean{


    /**
     * amount : 2588800
     * rate : 7
     * ticketBalance : 0
     * xstybBalance : 1760380
     */
    private String code;
    private Double amount;
    private Double rate;
    private Double ticketBalance;
    private Double xstybBalance;
    private String title;
    private String repaymentType;
    private String tenderTerm;
    private String incomeTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getTenderTerm() {
        return tenderTerm;
    }

    public void setTenderTerm(String tenderTerm) {
        this.tenderTerm = tenderTerm;
    }

    public String getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(String incomeTime) {
        this.incomeTime = incomeTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTicketBalance() {
        return ticketBalance;
    }

    public void setTicketBalance(Double ticketBalance) {
        this.ticketBalance = ticketBalance;
    }

    public Double getXstybBalance() {
        return xstybBalance;
    }

    public void setXstybBalance(Double xstybBalance) {
        this.xstybBalance = xstybBalance;
    }
}
