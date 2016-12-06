package com.mgjr.model.bean;

import java.io.Serializable;

/**
 * Created by xuxuefeng on 2016/9/1.
 */
public class AccountSettingBean extends BaseBean implements Serializable {

    /**
     * actname : 王杰海
     * address :
     * area :
     * bankname : ICBC
     * cardno : 6222021901010955902
     * city :
     * ctime : 1472456233000
     * id : 624
     * mid : 741
     * province :
     * rechargeamount : null
     * status : 0
     * withdrawamount : null
     */

    private AccountBankcardBean accountBankcard;
    /**
     * accountBankcard : {"actname":"王杰海","address":"","area":"","bankname":"ICBC","cardno":"6222021901010955902","city":"","ctime":1472456233000,"id":624,"mid":741,"province":"","rechargeamount":null,"status":0,"withdrawamount":null}
     * headImgUrl : http://test-documents.oss-cn-hangzhou.aliyuncs.com/null
     * member : {"activityflag":null,"attrs":null,"becomepttime":1434097199000,"becometime":null,"ctime":1471764598000,"ctype":1,"email":"10799087@qq.com","eok":1,"fromsource":1100,"hashcode":null,"id":741,"idcode":"431023198306116914","idtype":1,"lcs":1,"logip":"192.168.80.146","logpwd":"88be4aae641c5d42b8b757e90ce7199e","logtime":1472712880000,"membertype":4,"mobile":"13787131532","mok":1,"paypwd":"11111","pmid":0,"roles":"1,2","sourceid":null,"status":0,"truename":"王杰海","username":"wjk2009","vip":0}
     * nickname : 王杰海蓝灰红
     */

    private String headImgUrl;
    /**
     * activityflag : null
     * attrs : null
     * becomepttime : 1434097199000
     * becometime : null
     * ctime : 1471764598000
     * ctype : 1
     * email : 10799087@qq.com
     * eok : 1
     * fromsource : 1100
     * hashcode : null
     * id : 741
     * idcode : 431023198306116914
     * idtype : 1
     * lcs : 1
     * logip : 192.168.80.146
     * logpwd : 88be4aae641c5d42b8b757e90ce7199e
     * logtime : 1472712880000
     * membertype : 4
     * mobile : 13787131532
     * mok : 1
     * paypwd : 11111
     * pmid : 0
     * roles : 1,2
     * sourceid : null
     * status : 0
     * truename : 王杰海
     * username : wjk2009
     * vip : 0
     */

    private MemberBean member;
    private String nickname;

    public AccountBankcardBean getAccountBankcard() {
        return accountBankcard;
    }

    public void setAccountBankcard(AccountBankcardBean accountBankcard) {
        this.accountBankcard = accountBankcard;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 银行卡对象
     *
     * @author zhangkaiqin
     * @date 2016年7月12日
     */
    public static class AccountBankcardBean implements Serializable {

        private Integer id;

        private Integer mid;

        private String actname;

        private String banktruename;

        public String getBanktruename() {
            return banktruename;
        }

        public void setBanktruename(String banktruename) {
            this.banktruename = banktruename;
        }

        private String cardno;

        private String bankname;

        private Double rechargeamount;

        private Double withdrawamount;

        private String address;

        private String province;

        private String city;

        private String area;

        private long ctime;

        private Byte status = 0;

        private String bankicon;

        public String getBankicon() {
            return bankicon;
        }

        public void setBankicon(String bankicon) {
            this.bankicon = bankicon;
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

        public Double getRechargeamount() {
            return rechargeamount;
        }

        public void setRechargeamount(Double rechargeamount) {
            this.rechargeamount = rechargeamount;
        }

        public Double getWithdrawamount() {
            return withdrawamount;
        }

        public void setWithdrawamount(Double withdrawamount) {
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

}
