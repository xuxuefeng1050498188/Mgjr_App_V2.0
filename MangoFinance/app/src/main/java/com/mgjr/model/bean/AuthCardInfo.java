package com.mgjr.model.bean;

import java.io.Serializable;

/**
 * 银行信息
 * Created by Wangjeihai on 2016/8/29.
 */
public class AuthCardInfo implements Serializable {

    private int id;
    /**
     * 银行code
     **/
    private String bankcode;
    /**
     * 银行名称
     **/
    private String bankname;
    /**
     * 银行卡类型：1借记卡
     **/
    private int cardtype;
    /**
     * 交易方式：1认证支付
     **/
    private int type;
    /**
     * 单笔限额
     **/
    private double onemax;
    /**
     * 单日限额
     **/
    private double daymax;
    /**
     * 单月限额
     **/
    private double monthmax;
    /**
     * 描述
     **/
    private String remark;

    /**
     * 银行logo资源id
     */
    private int logoResId;

    private String bankicon;

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public int getCardtype() {
        return cardtype;
    }

    public void setCardtype(int cardtype) {
        this.cardtype = cardtype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getOnemax() {
        return onemax;
    }

    public void setOnemax(double onemax) {
        this.onemax = onemax;
    }

    public double getDaymax() {
        return daymax;
    }

    public void setDaymax(double daymax) {
        this.daymax = daymax;
    }

    public double getMonthmax() {
        return monthmax;
    }

    public void setMonthmax(double monthmax) {
        this.monthmax = monthmax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
    }
}
