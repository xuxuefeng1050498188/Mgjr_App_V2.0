package com.mgjr.model.bean;

/**
 * 银行卡对象
 *
 * @author zhangkaiqin
 * @date 2016年7月12日
 */
public class AccountBankcardBean {

    private Integer id;

    private Integer mid;

    private String actname;

    private String cardno;

    private String bankname;

    private double rechargeamount;

    private double withdrawamount;

    private String address;

    private String province;

    private String city;

    private String area;

    private long ctime;

    private Byte status = 0;

    private String bankicon;

    private String banktruename;

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    public String getBanktruename() {
        return banktruename;
    }

    public void setBanktruename(String banktruename) {
        this.banktruename = banktruename;
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

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public double getRechargeamount() {
        return rechargeamount;
    }

    public void setRechargeamount(double rechargeamount) {
        this.rechargeamount = rechargeamount;
    }

    public double getWithdrawamount() {
        return withdrawamount;
    }

    public void setWithdrawamount(double withdrawamount) {
        this.withdrawamount = withdrawamount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
