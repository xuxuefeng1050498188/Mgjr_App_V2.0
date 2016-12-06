package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MangoBoxBean extends BaseBean{

    private GiftMapBean giftMap;

    private List<CanUsedCouponListBean> canUsedCouponList;

    private List<CanUsedtHBListBean> canUsedtHBList;

    private List<OverCouponListBean> overCouponList;

    private List<OverHBListBean> overHBList;
    private List<UsedCouponListBean> usedCouponList;

    private List<UsedtHBListBean> usedtHBList;

    public GiftMapBean getGiftMap() {
        return giftMap;
    }

    public void setGiftMap(GiftMapBean giftMap) {
        this.giftMap = giftMap;
    }

    public List<CanUsedCouponListBean> getCanUsedCouponList() {
        return canUsedCouponList;
    }

    public void setCanUsedCouponList(List<CanUsedCouponListBean> canUsedCouponList) {
        this.canUsedCouponList = canUsedCouponList;
    }


    public List<OverCouponListBean> getOverCouponList() {
        return overCouponList;
    }

    public void setOverCouponList(List<OverCouponListBean> overCouponList) {
        this.overCouponList = overCouponList;
    }

    public List<OverHBListBean> getOverHBList() {
        return overHBList;
    }

    public void setOverHBList(List<OverHBListBean> overHBList) {
        this.overHBList = overHBList;
    }


    public List<UsedtHBListBean> getUsedtHBList() {
        return usedtHBList;
    }

    public void setUsedtHBList(List<UsedtHBListBean> usedtHBList) {
        this.usedtHBList = usedtHBList;
    }

    public static class GiftMapBean {
        private double allHbAmount;
        private int couponNum;
        private double hbAmount;
        private int hbNum;

        private int allCouponNum;

        public int getAllCouponNum() {
            return allCouponNum;
        }

        public void setAllCouponNum(int allCouponNum) {
            this.allCouponNum = allCouponNum;
        }

        public double getAllHbAmount() {
            return allHbAmount;
        }

        public void setAllHbAmount(double allHbAmount) {
            this.allHbAmount = allHbAmount;
        }

        public int getCouponNum() {
            return couponNum;
        }

        public void setCouponNum(int couponNum) {
            this.couponNum = couponNum;
        }

        public double getHbAmount() {
            return hbAmount;
        }

        public void setHbAmount(double hbAmount) {
            this.hbAmount = hbAmount;
        }

        public int getHbNum() {
            return hbNum;
        }

        public void setHbNum(int hbNum) {
            this.hbNum = hbNum;
        }
    }

    public static class CanUsedCouponListBean {
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

    public static class OverCouponListBean {
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public Object getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

    public static class OverHBListBean {
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

    public static class UsedtHBListBean {
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;
        private String loanTitle;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLoanTitle() {
            return loanTitle;
        }

        public void setLoanTitle(String loanTitle) {
            this.loanTitle = loanTitle;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

    public List<CanUsedtHBListBean> getCanUsedtHBList() {
        return canUsedtHBList;
    }

    public void setCanUsedtHBList(List<CanUsedtHBListBean> canUsedtHBList) {
        this.canUsedtHBList = canUsedtHBList;
    }

    public List<UsedCouponListBean> getUsedCouponList() {
        return usedCouponList;
    }

    public void setUsedCouponList(List<UsedCouponListBean> usedCouponList) {
        this.usedCouponList = usedCouponList;
    }

    public static class CanUsedtHBListBean{
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

    public static class UsedCouponListBean{
        private double amount;
        private long etime;
        private int flag;
        private long gtime;
        private int id;
        private int mid;
        private double noatamount;
        private double rate;
        private String remark;
        private int status;
        private long stime;
        private int tenderid;
        private int typeid;
        private String title;
        private String loanTitle;

        public String getLoanTitle() {
            return loanTitle;
        }

        public void setLoanTitle(String loanTitle) {
            this.loanTitle = loanTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getGtime() {
            return gtime;
        }

        public void setGtime(long gtime) {
            this.gtime = gtime;
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

        public double getNoatamount() {
            return noatamount;
        }

        public void setNoatamount(double noatamount) {
            this.noatamount = noatamount;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStime() {
            return stime;
        }

        public void setStime(long stime) {
            this.stime = stime;
        }

        public int getTenderid() {
            return tenderid;
        }

        public void setTenderid(int tenderid) {
            this.tenderid = tenderid;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }

}
