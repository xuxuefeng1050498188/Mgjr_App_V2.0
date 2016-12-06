package com.mgjr.model.bean;

/**
 * Created by xuxuefeng on 2016/8/31.
 */
public class RequestRechargeBean extends BaseBean {

    /**
     * amount : 110400.33
     * cardCode : ICBC
     * cardName : 工商银行
     * cardNo : 6222021901010955902
     * channel : 29
     * remark : 单笔5千，单日5万
     */

    private double amount;
    private String cardCode;
    private String cardName;
    private String cardNo;
    private int channel;
    private String remark;
    private String bankicon;

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    /**
     * amount : 200
     * bank : 6222021901010955901
     * chid : 29
     * code : 160909161656895694
     * ctime : 1473409016684
     * fee : 0
     * flowno : null
     * fromsource : 5
     * id : null
     * mid : 741
     * status : 0
     * thdata : null
     * trandate : null
     */

    private AccountRechargeBean accountRecharge;
    /**
     * activityflag : null
     * attrs : null
     * becomepttime : 1434097199000
     * becometime : null
     * ctime : 1471764598000
     * ctype : 1
     * email : 1050498188@qq.com
     * eok : 1
     * fromsource : 1100
     * hashcode : null
     * id : 741
     * idcode : 121212
     * idtype : 1
     * lcs : 1
     * logip : 192.168.80.182
     * logpwd : 88be4aae641c5d42b8b757e90ce7199e
     * logtime : 1473408672000
     * membertype : 4
     * mobile : 13787131532
     * mok : 0
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AccountRechargeBean getAccountRecharge() {
        return accountRecharge;
    }

    public void setAccountRecharge(AccountRechargeBean accountRecharge) {
        this.accountRecharge = accountRecharge;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }


    public static class AccountRechargeBean {
        private double amount;
        private String bank;
        private int chid;
        private String code;
        private long ctime;
        private int fee;
        private Object flowno;
        private int fromsource;
        private Object id;
        private int mid;
        private int status;
        private Object thdata;
        private Object trandate;

        public double getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public int getChid() {
            return chid;
        }

        public void setChid(int chid) {
            this.chid = chid;
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

        public Object getTrandate() {
            return trandate;
        }

        public void setTrandate(Object trandate) {
            this.trandate = trandate;
        }
    }

}
