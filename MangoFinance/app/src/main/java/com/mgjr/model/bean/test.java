package com.mgjr.model.bean;

/**
 * Created by xuxuefeng on 2016/9/19.
 */
public class test extends BaseBean {

    /**
     * ainfo : null
     * amount : 8
     * atime : null
     * code : 160919085541578720
     * ctime : 1474246541122
     * fee : 1
     * flowno : null
     * fromsource : 5
     * id : null
     * info : {"actName":"许雪峰","bankAddress":"","bankCode":"CBC","bankName":"CBC","cardNo":"6236682920007176421","citycode":"340100","provincecode":"340000","type":0}
     * mid : 2923
     * status : 0
     * thdata : null
     */

    private AccountWithdrawBean accountWithdraw;

    public AccountWithdrawBean getAccountWithdraw() {
        return accountWithdraw;
    }

    public void setAccountWithdraw(AccountWithdrawBean accountWithdraw) {
        this.accountWithdraw = accountWithdraw;
    }

    public static class AccountWithdrawBean {
        private Object ainfo;
        private int amount;
        private Object atime;
        private String code;
        private long ctime;
        private int fee;
        private Object flowno;
        private int fromsource;
        private Object id;
        private String info;
        private int mid;
        private int status;
        private Object thdata;

        public Object getAinfo() {
            return ainfo;
        }

        public void setAinfo(Object ainfo) {
            this.ainfo = ainfo;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Object getAtime() {
            return atime;
        }

        public void setAtime(Object atime) {
            this.atime = atime;
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

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public Object getFlowno() {
            return flowno;
        }

        public void setFlowno(Object flowno) {
            this.flowno = flowno;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getThdata() {
            return thdata;
        }

        public void setThdata(Object thdata) {
            this.thdata = thdata;
        }
    }
}
