package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/8/25.
 */
public class InvestProductDetailBean extends BaseBean {

    private double tenderLimit;
    private int tenderMembers;

    private boolean isNewcomerWelfare;
    private String guaranteeCompany;
    private String incomeType;
    private String incomeIncreaseType;
    private String hbqRate;

    public String getHbqRate() {
        return hbqRate;
    }

    public void setHbqRate(String hbqRate) {
        this.hbqRate = hbqRate;
    }

    public String getTenderTerm() {
        return tenderTerm;
    }

    public void setTenderTerm(String tenderTerm) {
        this.tenderTerm = tenderTerm;
    }

    private String tenderTerm;

    public String getIncomeIncreaseType() {
        return incomeIncreaseType;
    }

    public void setIncomeIncreaseType(String incomeIncreaseType) {
        this.incomeIncreaseType = incomeIncreaseType;
    }

    private LoanBean loan;
    private int tenderCount;
    private List<String> barrageList;

    public boolean isNewcomerWelfare() {
        return isNewcomerWelfare;
    }

    public void setNewcomerWelfare(boolean newcomerWelfare) {
        isNewcomerWelfare = newcomerWelfare;
    }

    public double getTenderLimit() {
        return tenderLimit;
    }

    public void setTenderLimit(double tenderLimit) {
        this.tenderLimit = tenderLimit;
    }

    public int getTenderMembers() {
        return tenderMembers;
    }

    public void setTenderMembers(int tenderMembers) {
        this.tenderMembers = tenderMembers;
    }

    public String getGuaranteeCompany() {
        return guaranteeCompany;
    }

    public void setGuaranteeCompany(String guaranteeCompany) {
        this.guaranteeCompany = guaranteeCompany;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public LoanBean getLoan() {
        return loan;
    }

    public void setLoan(LoanBean loan) {
        this.loan = loan;
    }

    public int getTenderCount() {
        return tenderCount;
    }

    public void setTenderCount(int tenderCount) {
        this.tenderCount = tenderCount;
    }

    public List<String> getBarrageList() {
        return barrageList;
    }

    public void setBarrageList(List<String> barrageList) {
        this.barrageList = barrageList;
    }



    private HqbBean hqb;

    public HqbBean getHqb() {
        return hqb;
    }

    public void setHqb(HqbBean hqb) {
        this.hqb = hqb;
    }


    private String shareTitle;
    private String shareContent;
    private String shareUrl;

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
