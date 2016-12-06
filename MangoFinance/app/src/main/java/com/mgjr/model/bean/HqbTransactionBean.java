package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HqbTransactionBean extends BaseBean {


    private double totalTender;
    private double totalTenderAll;

    private List<TransactionDetailListBean> transactionDetailList;
    private List<String> types;

    private double dsbjLoan;
    private double totalTenderAmount;

    public double getTotalTender() {
        return totalTender;
    }

    public void setTotalTender(double totalTender) {
        this.totalTender = totalTender;
    }

    public double getTotalTenderAll() {
        return totalTenderAll;
    }

    public void setTotalTenderAll(double totalTenderAll) {
        this.totalTenderAll = totalTenderAll;
    }

    public List<TransactionDetailListBean> getTransactionDetailList() {
        return transactionDetailList;
    }

    public void setTransactionDetailList(List<TransactionDetailListBean> transactionDetailList) {
        this.transactionDetailList = transactionDetailList;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public double getDsbjLoan() {
        return dsbjLoan;
    }

    public void setDsbjLoan(double dsbjLoan) {
        this.dsbjLoan = dsbjLoan;
    }

    public double getTotalTenderAmount() {
        return totalTenderAmount;
    }

    public void setTotalTenderAmount(double totalTenderAmount) {
        this.totalTenderAmount = totalTenderAmount;
    }

    public static class TransactionDetailListBean {
        private double amount;
        private long time;
        private String title;
        private String type;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /*====================================================*/



}
