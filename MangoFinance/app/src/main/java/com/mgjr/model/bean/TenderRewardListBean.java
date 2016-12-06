package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class TenderRewardListBean extends BaseBean {

    private String mobile;

    private List<LcsTenderRewardListBean> tenderRewardList;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<LcsTenderRewardListBean> getTenderRewardList() {
        return tenderRewardList;
    }

    public void setTenderRewardList(List<LcsTenderRewardListBean> tenderRewardList) {
        this.tenderRewardList = tenderRewardList;
    }

    public static class LcsTenderRewardListBean {
        private int id;
        private double jlamount;
        private int loantenderid;
        private long time;
        private int tjrid;
        private double tzamount;
        private String tzrUsername;
        private int tzrid;
        private int whenlcs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }



        public int getLoantenderid() {
            return loantenderid;
        }

        public void setLoantenderid(int loantenderid) {
            this.loantenderid = loantenderid;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTjrid() {
            return tjrid;
        }

        public void setTjrid(int tjrid) {
            this.tjrid = tjrid;
        }


        public double getJlamount() {
            return jlamount;
        }

        public void setJlamount(double jlamount) {
            this.jlamount = jlamount;
        }

        public String getTzrUsername() {
            return tzrUsername;
        }

        public void setTzrUsername(String tzrUsername) {
            this.tzrUsername = tzrUsername;
        }

        public double getTzamount() {
            return tzamount;
        }

        public void setTzamount(double tzamount) {
            this.tzamount = tzamount;
        }

        public int getTzrid() {
            return tzrid;
        }

        public void setTzrid(int tzrid) {
            this.tzrid = tzrid;
        }

        public int getWhenlcs() {
            return whenlcs;
        }

        public void setWhenlcs(int whenlcs) {
            this.whenlcs = whenlcs;
        }
    }
}
