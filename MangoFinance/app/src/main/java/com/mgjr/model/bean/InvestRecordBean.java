package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class InvestRecordBean extends BaseBean {

    private TenderBean firstTender;
    private TenderBean tenderKing;
    private String code;
    private String dayloanflag;
    private List<TenderBean> tenderList;

    public String getDayloanflag() {
        return dayloanflag;
    }

    public void setDayloanflag(String dayloanflag) {
        this.dayloanflag = dayloanflag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TenderBean getFirstTender() {
        return firstTender;
    }

    public void setFirstTender(TenderBean firstTender) {
        this.firstTender = firstTender;
    }

    public TenderBean getTenderKing() {
        return tenderKing;
    }

    public void setTenderKing(TenderBean tenderKing) {
        this.tenderKing = tenderKing;
    }

    public List<TenderBean> getTenderList() {
        return tenderList;
    }

    public void setTenderList(List<TenderBean> tenderList) {
        this.tenderList = tenderList;
    }

    /**
     * 投资记录（金芒果新手福利等）
     *
     * @author zhangkaiqin
     * @date 2016年7月12日
     */
    public static class TenderBean {

        private Integer id;

        private String code;

        private Integer mid;

        private Double amount;

        private Double experienceAmount;

        private Integer loanid;

        private Double extraAmount;

        private Integer type;

        private long ctime;

        private Integer status;

        private Integer fromsource;

        private String seckillTime;

        public String getSeckillTime() {
            return seckillTime;
        }

        public void setSeckillTime(String seckillTime) {
            this.seckillTime = seckillTime;
        }

        //额外参数（member表里面的用户名）
        private String username;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getMid() {
            return mid;
        }

        public void setMid(Integer mid) {
            this.mid = mid;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Double getExperienceAmount() {
            return experienceAmount;
        }

        public void setExperienceAmount(Double experienceAmount) {
            this.experienceAmount = experienceAmount;
        }

        public Integer getLoanid() {
            return loanid;
        }

        public void setLoanid(Integer loanid) {
            this.loanid = loanid;
        }

        public Double getExtraAmount() {
            return extraAmount;
        }

        public void setExtraAmount(Double extraAmount) {
            this.extraAmount = extraAmount;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getFromsource() {
            return fromsource;
        }

        public void setFromsource(Integer fromsource) {
            this.fromsource = fromsource;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }


}