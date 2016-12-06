package com.mgjr.model.bean;


/**
 * Created by xuxuefeng on 2016/9/21.
 */
public class ActivityGitBean {
    /**
     * 标记Bean指向的条目是否选中,默认未选中
     */
    private boolean isSelected;

    public boolean isUseable() {
        return isUseable;
    }

    public void setUseable(boolean useable) {
        isUseable = useable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * 该红包或者加息券是否可用,默认可用
     */
    private boolean isUseable = true;


    private Integer id;

    private Integer mid;

    private Integer typeid;

    private double amount;

    private double rate;

    private Integer flag;

    private long stime;

    private long etime;

    private long gtime;

    private String remark;

    private String title;

    private Integer status;//红包状态，0表示激活，1表示未激活

    private double noatamount;//未激活金额 可投资，不可提现

    private Integer tenderid;//  记录投资ID

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public long getStime() {
        return stime;
    }

    public void setStime(long stime) {
        this.stime = stime;
    }

    public long getEtime() {
        return etime;
    }

    public void setEtime(long etime) {
        this.etime = etime;
    }

    public long getGtime() {
        return gtime;
    }

    public void setGtime(long gtime) {
        this.gtime = gtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getNoatamount() {
        return noatamount;
    }

    public void setNoatamount(double noatamount) {
        this.noatamount = noatamount;
    }

    public Integer getTenderid() {
        return tenderid;
    }

    public void setTenderid(Integer tenderid) {
        this.tenderid = tenderid;
    }
}
