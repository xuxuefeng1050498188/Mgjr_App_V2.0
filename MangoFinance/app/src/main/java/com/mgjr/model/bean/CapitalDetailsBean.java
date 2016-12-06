package com.mgjr.model.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class CapitalDetailsBean extends BaseBean {

    private double acountBalance;

    private Map actionMap;
    private Double withdrawingAmount;

    private List<BillDetailListBean> billDetailList;

    public double getAcountBalance() {
        return acountBalance;
    }

    public void setAcountBalance(double acountBalance) {
        this.acountBalance = acountBalance;
    }


    public Double getWithdrawingAmount() {
        return withdrawingAmount;
    }

    public void setWithdrawingAmount(Double withdrawingAmount) {
        this.withdrawingAmount = withdrawingAmount;
    }

    public List<BillDetailListBean> getBillDetailList() {
        return billDetailList;
    }

    public void setBillDetailList(List<BillDetailListBean> billDetailList) {
        this.billDetailList = billDetailList;
    }

    public Map getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map actionMap) {
        this.actionMap = actionMap;
    }

    public static class BillDetailListBean {
        private String action;
        private String adata;
        private double amount;

        private long ctime;
        private int ctype;
        private int id;
        private int mid;
        private String remark;
        private String tname;

        public String getAdata() {
            return adata;
        }

        public void setAdata(String adata) {
            this.adata = adata;
        }

        public Map<String,String> getRemark() {
            Gson son = new GsonBuilder().create();
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            Map<String,String> map = son.fromJson(remark,type);
            return map;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }



        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getCtype() {
            return ctype;
        }

        public void setCtype(int ctype) {
            this.ctype = ctype;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }



        public static class Adata{
            private Double aa_amount;
            private Double tt_amount;
            private Double fz_amount;

            public Double getAa_amount() {
                return aa_amount;
            }

            public void setAa_amount(Double aa_amount) {
                this.aa_amount = aa_amount;
            }

            public Double getTt_amount() {
                return tt_amount;
            }

            public void setTt_amount(Double tt_amount) {
                this.tt_amount = tt_amount;
            }

            public Double getFz_amount() {
                return fz_amount;
            }

            public void setFz_amount(Double fz_amount) {
                this.fz_amount = fz_amount;
            }
        }
//
        public static class Remark{

        private String message;
        private int tenderId;
        private int loanId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getTenderId() {
            return tenderId;
        }

        public void setTenderId(int tenderId) {
            this.tenderId = tenderId;
        }

        public int getLoanId() {
            return loanId;
        }

        public void setLoanId(int loanId) {
            this.loanId = loanId;
        }
    }

    }
}
